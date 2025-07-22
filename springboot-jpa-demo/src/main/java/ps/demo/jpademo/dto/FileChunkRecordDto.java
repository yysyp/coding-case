package ps.demo.jpademo.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileChunkRecordDto {

    private Long id;
    private String fileMd5;
    private long fileSize;
    private long lastModifiedTime;
    private String fileType;

    private long totalChunks;
    private String fileName;
    private String description;

    private List<String> chunkMD5s;

}