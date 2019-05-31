package fi.omat.johneagle.filebox.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation. Check if the password is the current suers old one (and not empty).
 * Main logic is in Constraint class that is connected to this.
 * As this mainly handles only that it can be used as annotation.
 *
 * @see PasswordValidator
 */
@Documented
@Constraint(validatedBy = OldPasswordValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface OldPassword {
    String message() default "Password incorrect!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
