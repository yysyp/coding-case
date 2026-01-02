package ps.demo.jpademo.entity;

import jakarta.persistence.*;
import lombok.*;
import ps.demo.jpademo.dto.FileChunkRecordDto;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="file_records")
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileMd5;
    private long fileSize;
    private long lastModifiedTime;
    private String fileType;

    private String path;

    private long totalChunks;
    private String fileName;
    private String description;

    private String status;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }


    @OneToMany(mappedBy = "fileRecord")
    @Builder.Default
    private Set<ChunkRecord> chunkRecordSet = new HashSet<>();

    public void addChunkRecords(ChunkRecord chunkRecord) {
        this.chunkRecordSet.add(chunkRecord);
        chunkRecord.setFileRecord(this);
    }

    public void removeChunkRecords(ChunkRecord chunkRecord) {
        this.chunkRecordSet.remove(chunkRecord);
        chunkRecord.setFileRecord(null);
    }

    public static FileRecord fromDto(FileChunkRecordDto fileChunkRecordDto) {
        FileRecord fr = new FileRecord();
        fr.fileMd5 = fileChunkRecordDto.getFileMd5();
        fr.fileSize = fileChunkRecordDto.getFileSize();
        fr.lastModifiedTime = fileChunkRecordDto.getLastModifiedTime();
        fr.fileType = fileChunkRecordDto.getFileType();
        fr.totalChunks = fileChunkRecordDto.getTotalChunks();
        fr.fileName = fileChunkRecordDto.getFileName();
        fr.description = fileChunkRecordDto.getDescription();
        return fr;
    }

}