package fi.omat.johneagle.filebox.domain.entities;

import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // For visibility RESTRICT to tell who can see.
    @ElementCollection
    private List<String> specificCanSee = new ArrayList<>();

    // File Header
    private String filename;
    private String contentType;
    private Long contentLength;

    // Actual file or well more like its digital representation.
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    // Owner
    @OneToOne
    private Account owner;
}
