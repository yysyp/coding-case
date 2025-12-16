package ps.demo.zglj.mq;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracking")
@Getter
@Setter
public class TrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long messageId;

    @Column(nullable = false)
    private Long subscriberId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String details;

    @Column
    private String subscriberName;

    @Column
    private String subscriberEndpoint;
}