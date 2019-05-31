package fi.omat.johneagle.filebox.domain.validationmodels;

import fi.omat.johneagle.filebox.validators.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

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
public class PersonInfoModel {
    // Ensures old password is given.
    @OldPassword
    private String oldPassword;

    // Checks if password is valid
    @Password
    private String newPassword;

    @NotEmpty
    private String newPasswordAgain;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    // Custom nickname check.
    @Nickname
    private String nickname;

    // Custom email validation
    @ValidEmail
    private String email;

    // Checks if date is in the past
    @Past
    private LocalDate born;

    private String address;
    private String addressNumber;
    private String city;
    private String phoneNumber;
}
