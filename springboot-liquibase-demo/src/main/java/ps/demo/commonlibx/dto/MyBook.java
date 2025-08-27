package ps.demo.commonlibx.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MyBook implements java.io.Serializable {

    private Long id;
    private String title;
    private String url;
    private String myText;
    private byte[] myByte;
    private Instant instant;

}
