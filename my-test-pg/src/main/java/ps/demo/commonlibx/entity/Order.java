package ps.demo.commonlibx.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tbl_order")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("total_price")
    private BigDecimal totalPrice;

    @TableField("payment_method")
    private String paymentMethod;

    @TableField("transaction_id")
    private String transactionId;

    @TableField("created_at")
    private Date createdAt;

    @TableField("status")
    private String status;

}
