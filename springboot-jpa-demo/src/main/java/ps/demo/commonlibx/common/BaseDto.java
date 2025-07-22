/**
 * 
 */
package ps.demo.commonlibx.common;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Copy from BaseEntity.
 */
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String createdBy;

	private Instant createdOn;

	private Boolean isActive;
	private Boolean isLogicalDeleted;

	private String modifiedBy;

	private Instant modifiedOn;

}
