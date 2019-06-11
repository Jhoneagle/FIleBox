package fi.omat.johneagle.filebox.domain.validationmodels;

import org.springframework.web.multipart.MultipartFile;
import fi.omat.johneagle.filebox.validators.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Profile picture validation model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel {
    @Image
    private MultipartFile file;
}
