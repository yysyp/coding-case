package ps.demo.commonlibx.dto;

import lombok.*;
import ps.demo.commonlibx.common.BaseResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCartResponse extends BaseResponse {

    private Data data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Data {

        private Long curtId;
        private Long userId;
        private BigDecimal totalPrice;
        private Date createdAt;
        private List<Item> items;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Item {

        private Long cartItemId;
        private Long productId;
        private Integer quantity;
        private BigDecimal totalPrice;
    }


}
