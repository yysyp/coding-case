package ps.demo.jpademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.jpademo.entity.ChunkRecord;


import java.util.List;
import java.util.Optional;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface ChunkRepository extends JpaRepository<ChunkRecord, Long> {
    Optional<ChunkRecord> findByFileRecordIdAndChunkMd5AndChunkIndex(Long fileRecordId, String chunkMd5, Long chunkIndex);

    Optional<ChunkRecord> findByFileRecordIdAndChunkMd5AndChunkIndexAndStatus(Long fileRecordId, String chunkMd5, long chunkIndex, String status);

    List<ChunkRecord> findByFileRecordId(Long fileRecordId);


}
