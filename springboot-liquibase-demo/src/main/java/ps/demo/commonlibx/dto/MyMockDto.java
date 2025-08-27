

package ps.demo.commonlibx.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MyMockDto {

    private Long id;
    private String uri;
    private Boolean regexMatch;
    private String method;
    private Integer status;
    private String headers;
    private String body;


}


