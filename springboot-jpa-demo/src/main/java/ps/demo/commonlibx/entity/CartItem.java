package ps.demo.commonlibx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("cart_item")
public class CartItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("cart_id")
    private Long cartId;

    @TableField("product_id")
    private Long productId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("total_price")
    private BigDecimal totalPrice;


}
