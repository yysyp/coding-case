package ps.demo.commonlibx.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiClient {

    private String name;
    private String url;
    private String key;

}
