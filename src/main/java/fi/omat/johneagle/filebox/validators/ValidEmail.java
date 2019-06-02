package fi.omat.johneagle.filebox.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator for emails. More restrict then what javax.validation.constraints offers.
 */
@Email(message="Provide a valid email address!")
@Pattern(regexp="^$|.+@.+\\..+", message="Provide a valid email address!")
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidEmail {
    String message() default "Provide a valid email address!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
