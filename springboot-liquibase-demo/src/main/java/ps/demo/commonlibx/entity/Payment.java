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
@TableName("payment")
public class Payment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("payment_method")
    private String paymentMethod;

    @TableField("card_no")
    private String cardNo;

    @TableField("expiry_date")
    private Date expiryDate;

    @TableField("cvc_no")
    private String cvcNo;

    @TableField("transaction_id")
    private String transactionId;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("created_at")
    private Date createdAt;

}
