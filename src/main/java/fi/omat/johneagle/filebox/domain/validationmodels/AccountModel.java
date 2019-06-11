package fi.omat.johneagle.filebox.domain.validationmodels;

import javax.validation.constraints.NotEmpty;
import fi.omat.johneagle.filebox.validators.FieldMatch;
import fi.omat.johneagle.filebox.validators.Nickname;
import fi.omat.johneagle.filebox.validators.Password;
import fi.omat.johneagle.filebox.validators.Username;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation object for registration form.
 *
 * @see FieldMatch
 * @see Username
 * @see Nickname
 * @see Password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "password", second = "passwordAgain", message = "The password fields must match!") // Checks if both fields match perfectly.
public class AccountModel {
    // Custom username check.
    @Username
    private String username;

    // Custom password check.
    @Password
    private String password;

    @NotEmpty
    private String passwordAgain;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    // Custom nickname check.
    @Nickname
    private String nickname;
}
