package ps.demo.lqbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileResultDto {

    private Long fileRecordId;
    private Boolean exist;


}
