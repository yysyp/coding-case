package ps.demo.zglj.dto;

import lombok.*;
import ps.demo.zglj.common.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartResponse extends BaseResponse {

    private Data data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public static class Data {
        private Long cartId;
    }

}
