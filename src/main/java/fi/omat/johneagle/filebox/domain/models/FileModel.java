package fi.omat.johneagle.filebox.domain.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Saver and more light version of file entities to shown for user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileModel {
    private String filename;
    private String contentType;
    private Long contentLength;
    private LocalDateTime timestamp;
    private Long id;
}
