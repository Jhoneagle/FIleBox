package fi.omat.johneagle.filebox.domain.validationmodels;

import fi.omat.johneagle.filebox.validators.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel {
    @Image
    private MultipartFile file;
}
