package ps.demo.jpademo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="chunk_records")
public class ChunkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chunkMd5;

    private long chunkIndex;

    private String status;

    @ManyToOne
    private FileRecord fileRecord;


}