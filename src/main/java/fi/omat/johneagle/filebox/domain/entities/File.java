package fi.omat.johneagle.filebox.domain.entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import java.time.LocalDateTime;
import javax.persistence.*;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Database table for files.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File extends AbstractPersistable<Long> {
    private FileVisibility visibility;
    private LocalDateTime timestamp;

    // File Header
    private String filename;
    private String contentType;
    private Long contentLength;

    // Actual file or well more like its digital representation.
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    // Owner
    @OneToOne
    private Account owner;
}
