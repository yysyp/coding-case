package ps.demo.commonlibx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Positive;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveFromCartDto implements Serializable {

    @Positive(message = "userId is invalid")
    private Long userId;

    @Positive(message = "cartId is invalid")
    private Long cartId;

    @Positive(message = "productId is invalid")
    private Long productId;

    @Positive(message = "quantity should not be negative or zero")
    private Integer quantity;


}
