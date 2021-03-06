package fi.omat.johneagle.filebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import fi.omat.johneagle.filebox.domain.entities.Account;

/**
 * Account database table interface for JPA.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByNickname(String nickname);
    List<Account> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
