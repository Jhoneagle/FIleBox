package fi.omat.johneagle.filebox.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
