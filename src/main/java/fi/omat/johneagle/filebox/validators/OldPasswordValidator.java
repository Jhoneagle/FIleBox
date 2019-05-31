package fi.omat.johneagle.filebox.validators;

import fi.omat.johneagle.filebox.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Constraint class to handle the logic of validator annotation that has this connected into it.
 *
 * @see Password
 */
public class OldPasswordValidator implements ConstraintValidator<Password, String> {
    @Autowired
    private AccountService accountService;

    /**
     * Checks if the password is empty so that validation is ignored or if it is equivalent with old current users old one.
     *
     * @param password value that is contained by the field in the object which is currently been validated that this annotation has been annotated.
     * @param constraintValidatorContext context stuff of the constraint and validator annotation. not that important.
     *
     * @return <code>true</code> if password passes validation and otherwise <code>false</code>.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        String violationMessage = "";

        if (password.equals("")) {
            return true;
        }

        if (!accountService.equivalentPassword(password)) {
            violationMessage = "Password is not correct!";
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
