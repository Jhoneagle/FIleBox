package fi.omat.johneagle.filebox.domain.validationmodels;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotEmpty;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import fi.omat.johneagle.filebox.validators.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation model for file download.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFile {
    @File
    private MultipartFile file;

    FileVisibility fileVisibility;
}
