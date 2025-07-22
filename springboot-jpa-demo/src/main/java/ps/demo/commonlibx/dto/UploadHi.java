package ps.demo.commonlibx.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UploadHi {

    private int index;

    private String fileStr;

    private int num;

    private long length;

}
