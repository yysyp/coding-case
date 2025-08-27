package ps.demo.commonlibx.common;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BasePageReq implements Serializable {

    private long current = 1;

    //@ApiModelProperty(value = "Page size. Pass -1 will query all records in one page.", example="10")
    private long size = 10;

    private List<OrderBy> orderBys = new ArrayList<>();

    @Getter
    @Setter
    public static class OrderBy {

        //@ApiModelProperty(value = "Order by column", example = "id")
        private String key = "id";
        private Boolean asc = false;
    }

}
