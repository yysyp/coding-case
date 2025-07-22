package ps.demo.commonlibx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.demo.commonlibx.common.ClientErrorException;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.commonlibx.common.ProjConstant;
import ps.demo.commonlibx.common.ServerErrorException;
import ps.demo.commonlibx.entity.*;
import ps.demo.commonlibx.repository.*;
import ps.demo.commonlibx.dto.PlaceOrderRequest;
import ps.demo.commonlibx.dto.PlaceOrderResponse;
import ps.demo.commonlibx.entity.*;
import ps.demo.commonlibx.repository.*;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {

        // Validate cart
        Cart cart = cartMapper.getCartAndItems(request.getCartId());

        if(cart == null || !cart.getUserId().equals(request.getUserId())) {
            throw new ClientErrorException(CodeEnum.INVALID_ID);
        }

        // Create order from cart
        Order order = Order.builder().userId(cart.getUserId())
                .totalPrice(cart.getTotalPrice())
                .paymentMethod(ProjConstant.PAYMENT_METHOD)
                .transactionId(UUID.randomUUID().toString())
                .createdAt(ProjConstant.getNowDate()).status(ProjConstant.PENDING).build();
        orderMapper.insert(order);

        // Validate & update stock & create order items
        // TODO: To use batch update.
        for(CartItem item : cart.getItems()) {
            Stock stock = stockMapper.selectById(item.getProductId());
            if(stock.getQuantity() < item.getQuantity()) {
                throw new ServerErrorException(CodeEnum.NO_ENOUGH_STOCK);
            }
            Integer originStockQuantity = stock.getQuantity();
            stock.setQuantity(originStockQuantity - item.getQuantity());
            int updated = stockMapper.update(stock, new QueryWrapper<Stock>()
                    .eq("product_id", item.getProductId())
                    .eq("quantity", originStockQuantity));
            //Make sure the record updating is not conflicting.
            if (updated != 1) {
                throw new ServerErrorException(CodeEnum.CONCURRENT_OPERATION);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductId(item.getProductId());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItemMapper.insert(orderItem);
        }

        //Store payment information
        Payment payment = Payment.builder().orderId(order.getId())
                .createdAt(ProjConstant.getNowDate())
                .amount(cart.getTotalPrice())
                .cardNo(request.getCardNo())
                .cvcNo(request.getCvc())
                .expiryDate(request.getExpiryDate()).build();
        paymentMapper.insert(payment);

        //Clean Cart and cartItems
        cartItemMapper.deleteBatchIds(cart.getItems().stream().map(e -> e.getId())
                .collect(Collectors.toList()));
        cartMapper.deleteById(cart.getId());

        // Build response
        PlaceOrderResponse.Data data = PlaceOrderResponse.Data.builder()
                .orderId(order.getId()).total(order.getTotalPrice())
                .status(ProjConstant.PENDING).build();
        return new PlaceOrderResponse(data);

    }

}
