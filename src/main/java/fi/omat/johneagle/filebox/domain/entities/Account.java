package fi.omat.johneagle.filebox.domain.entities;

import org.springframework.data.jpa.domain.AbstractPersistable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Database User table. Containing all the info of the users.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractPersistable<Long> {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String nickname;
    private String address;
    private String addressNumber;
    private String city;
    private String phoneNumber;
    private String email;
    private LocalDate born;

    // Images the account has added.
    @OneToMany(mappedBy = "owner")
    private List<Image> images = new ArrayList<>();

    // Authorities account has.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities = new ArrayList<>();

    /**
     * Custom simplified tostring to prevent infinite loop because of the friendship and friendshipState update double users.
      */
    @Override
    public String toString() {
        return username;
    }

    /**
     * Simplified way to get users full name without having it as one field still.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
