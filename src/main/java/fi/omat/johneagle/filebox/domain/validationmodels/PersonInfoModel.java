package fi.omat.johneagle.filebox.domain.validationmodels;

import java.time.LocalDate;
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
public class PersonInfoModel {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    // Custom email validation
    @ValidEmail
    private String email;

    private LocalDate born;
    private String address;
    private String addressNumber;
    private String city;
    private String phoneNumber;
}
