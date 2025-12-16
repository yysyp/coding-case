package ps.demo.zglj.entity.listener;

import java.time.Instant;

public interface AutoFill {

    Instant getCreatedAt();

    void setCreatedAt(Instant createdAt);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    Instant getUpdatedAt();

    void setUpdatedAt(Instant updatedAt);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);
}