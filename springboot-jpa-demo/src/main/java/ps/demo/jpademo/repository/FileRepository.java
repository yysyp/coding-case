package ps.demo.jpademo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ps.demo.jpademo.entity.FileRecord;


import java.time.LocalDateTime;
import java.util.Optional;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface FileRepository extends JpaRepository<FileRecord, Long> {

    Optional<FileRecord> findFirstByFileMd5AndFileSizeAndLastModifiedTimeAndFileType(String fileMd5, long fileSize, long lastModifiedTime, String fileType);

    @Modifying
    @Transactional
    @Query("update ps.demo.jpademo.entity.FileRecord f set f.status = :status, f.updatedAt = :updatedAt where f.id = :fileId and f.status = :oldStatus")
    int updateFileRecordStatus(@Param("status") String status, @Param("updatedAt") LocalDateTime updatedAt, @Param("fileId") Long fileId, @Param("oldStatus") String oldStatus);

}
