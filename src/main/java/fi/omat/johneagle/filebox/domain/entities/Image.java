package fi.omat.johneagle.filebox.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Database image table. Contains all the images in the application that users are posting.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image extends AbstractPersistable<Long> {
    private String name;
    private String description;
    private LocalDateTime timestamp;

    // Image headers.
    private String filename;
    private String contentType;
    private Long contentLength;

    // Actual image or well more like its digital representation.
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    @OneToOne
    private Account owner;
}
