package ps.demo.commonlibx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.demo.commonlibx.common.ClientErrorException;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.commonlibx.common.ProjConstant;
import ps.demo.commonlibx.common.ServerErrorException;
import ps.demo.commonlibx.entity.Stock;
import ps.demo.commonlibx.repository.CartItemMapper;
import ps.demo.commonlibx.repository.CartMapper;
import ps.demo.commonlibx.repository.ProductMapper;
import ps.demo.commonlibx.repository.StockMapper;
import ps.demo.commonlibx.dto.GetCartResponse;
import ps.demo.commonlibx.entity.Cart;
import ps.demo.commonlibx.entity.CartItem;
import ps.demo.commonlibx.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;


    /**
     * Get cart & cartItems info by userId and cartId.
     * @param userId user id
     * @param cartId user's cart id
     * @return detail cart & cart items
     */
    @Transactional(readOnly = true)
    public GetCartResponse getCartDetail(Long userId, Long cartId) {
        //Note: assuming userId is already validated.

        //Validate userId and cartId matches
        Cart cart = cartMapper.getCartAndItems(cartId);
        if (cart == null || !userId.equals(cart.getUserId())) {
            throw new ClientErrorException(CodeEnum.INVALID_ID);
        }

        List<GetCartResponse.Item> items = new ArrayList<>();
        cart.getItems().forEach(e -> {
            items.add(GetCartResponse.Item.builder().cartItemId(e.getId())
                    .productId(e.getProductId()).totalPrice(e.getTotalPrice())
                    .quantity(e.getQuantity()).build());
        });

        GetCartResponse.Data data = GetCartResponse.Data.builder()
                .curtId(cartId).userId(userId).totalPrice(cart.getTotalPrice())
                .createdAt(cart.getCreatedAt()).items(items).build();
        return new GetCartResponse(data);
    }

    /**
     * Additional adding product to cart, if cart doesn't exist then create.
     * @param userId user id
     * @param productId product to add to cart
     * @param quantity quantity of the product to add to cart.
     * @return cart id
     */
    @Transactional
    public Long addToCart(Long userId, Long productId, Integer quantity) {

        //Note: assuming userId is already validated.

        // Validate product exists
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new ClientErrorException(CodeEnum.INVALID_ID);
        }

        // Validate stock, this is a 'soft' check
        /*
         * TODO: about the stock calculation, it can be enhanced to
         *  separate different stocks, i.e. display stock, actual stock etc.
         *  then we need to add lock (pessimistic / optimistic lock) to control
         *  the product display stock after adding/removing product.
         *
         */
        Stock stock = stockMapper.selectById(productId);
        if (stock == null || stock.getQuantity() < quantity) {
            throw new ServerErrorException(CodeEnum.NO_ENOUGH_STOCK);
        }

        // Get user's cart
        Cart cart = cartMapper.selectOne(new QueryWrapper<Cart>()
                .eq("user_id", userId));

        // Create cart if doesn't exist
        /* Note: In cart table, the user_id has unique constraint,
        so in concurrency scenario, the unique constraint will guarantee
        only one record will be created successfully for one user.
         */
        if (cart == null) {
            cart = Cart.builder().userId(userId).createdAt(ProjConstant.getNowDate()).build();
            cartMapper.insert(cart);
            BigDecimal cartItemTotalPrice = ProjConstant.multiply(product.getPrice(), quantity);
            CartItem cartItem = CartItem.builder().cartId(cart.getId())
                    .productId(productId).quantity(quantity).totalPrice(cartItemTotalPrice).build();
            cartItemMapper.insert(cartItem);
        } else {
            List<CartItem> cartItemList = cartItemMapper.selectList(new QueryWrapper<CartItem>()
                    .eq("cart_id", cart.getId()));
            cart.setItems(cartItemList);
            Optional<CartItem> itemOption = cart.getItems().stream().filter(e -> e.getProductId()
                    .equals(productId)).findAny();
            CartItem cartItem = null;
            if (itemOption.isPresent()) {
                cartItem = itemOption.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemMapper.updateById(cartItem);
            } else {
                cartItem = CartItem.builder().cartId(cart.getId())
                        .productId(productId).quantity(quantity).build();
                cartItemMapper.insert(cartItem);
            }
        }

        reCalculateTotalPrice(cart);
        return cart.getId();
    }

    /**
     * Remove number of product from cart.
     * @param userId user id
     * @param cartId cart id
     * @param productId product to be removed from cart
     * @param quantity the number of product to be removed from cart.
     * @return
     */
    @Transactional
    public Long removeFromCart(Long userId, Long cartId, Long productId, Integer quantity) {

        //Note: assuming userId is already validated.

        //Validate userId and cartId matches
        Cart cart = cartMapper.getCartAndItems(cartId);
        if (cart == null || !userId.equals(cart.getUserId())) {
            throw new ClientErrorException(CodeEnum.INVALID_ID);
        }

        Optional<CartItem> itemOption = cart.getItems().stream().filter(e -> e.getProductId()
                .equals(productId)).findAny();
        if (!itemOption.isPresent()) {
            throw new ClientErrorException(CodeEnum.INVALID_ID);
        }

        CartItem cartItem = itemOption.get();
        Integer newQuantity = cartItem.getQuantity() - quantity;
        if (newQuantity <= 0) {
            cartItemMapper.deleteById(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemMapper.updateById(cartItem);
        }
        reCalculateTotalPrice(cart);

        return cart.getId();
    }

    private void reCalculateTotalPrice(Cart cart) {
        //Re-calculate total price of the cart.
        List<CartItem> cartItemList = cartItemMapper.selectList(new QueryWrapper<CartItem>()
                .eq("cart_id", cart.getId()));
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItemList) {
            Product prd = productMapper.selectById(item.getProductId());
            BigDecimal itemTotalPrice = ProjConstant.multiply(prd.getPrice(), item.getQuantity());
            item.setTotalPrice(itemTotalPrice);
            //TODO: to change to update in batch.
            cartItemMapper.updateById(item);
            cartTotalPrice = cartTotalPrice.add(itemTotalPrice);
        }
        cart.setItems(cartItemList);
        cart.setTotalPrice(cartTotalPrice);
        cartMapper.updateById(cart);
    }

}