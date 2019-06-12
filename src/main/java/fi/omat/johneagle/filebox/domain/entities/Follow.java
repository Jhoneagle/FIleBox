package fi.omat.johneagle.filebox.domain.entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Database table for seeing who follows who.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends AbstractPersistable<Long> {
    private LocalDateTime timestamp;

    @ManyToOne
    private Account followed;

    @ManyToOne
    private Account follower;

    /**
     * Check Account table for this.
     *
     * @see Account#toString()
     */
    @Override
    public String toString() {
        return timestamp.toString();
    }
}
