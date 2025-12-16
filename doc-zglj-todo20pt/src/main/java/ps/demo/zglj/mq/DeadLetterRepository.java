package ps.demo.zglj.mq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadLetterRepository extends JpaRepository<DeadLetterEntity, Long> {

    Page<DeadLetterEntity> findByMessageId(Long messageId, Pageable pageable);
}