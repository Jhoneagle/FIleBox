package fi.omat.johneagle.filebox.validators;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Constraint class to handle the logic of validator annotation that has this connected into it.
 *
 * @see Image
 */
public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    private List<String> types = Arrays.asList("image/png", "image/gif", "image/jpeg", "image/jpg");

    /**
     * Checks from database if the nickname has been already taken and also checks if its preferred size.
     *
     * @param file value that is contained by the field in the object which is currently been validated that this annotation has been annotated.
     * @param constraintValidatorContext context stuff of the constraint and validator annotation. not that important.
     *
     * @return <code>true</code> if nickname hasn't been taken yet and otherwise <code>false</code>.
     */
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        String violationMessage = "";

        if (file.isEmpty()) {
            violationMessage = "File must not be empty!";
        } else if (!types.contains(file.getContentType())) {
            violationMessage = "File must be actual image file!";
        } else if (file.getSize() > 3145728) {
            violationMessage = "File must not excess 3MB!";
        }

        if (!(violationMessage.equals(""))) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(violationMessage)
                    .addConstraintViolation().disableDefaultConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}
