package ps.demo.jpademo.mq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends JpaRepository<TrackingEntity, Long> {

    Page<TrackingEntity> findByMessageId(Long messageId, Pageable pageable);

    Page<TrackingEntity> findByStatus(String status, Pageable pageable);
}