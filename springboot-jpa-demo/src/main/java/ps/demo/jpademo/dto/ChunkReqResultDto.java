package ps.demo.jpademo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChunkReqResultDto {

    private Long fileRecordId;
    private String chunkMd5;
    private Long chunkIndex;
    private Boolean exist;

}
