package fi.omat.johneagle.filebox.domain.validationmodels;

import javax.validation.constraints.NotEmpty;
import fi.omat.johneagle.filebox.validators.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation object for updating personal info.
 *
 * @see OldPassword
 * @see FieldMatch
 * @see Nickname
 * @see Password
 * @see ValidEmail
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "newPassword", second = "newPasswordAgain", message = "The password fields must match!") // Checks if both fields match perfectly.
public class ChangePasswordModel {
    // Ensures old password is given.
    @OldPassword
    private String oldPassword;

    // Checks if password is valid
    @Password
    private String newPassword;

    @NotEmpty
    private String newPasswordAgain;
}
