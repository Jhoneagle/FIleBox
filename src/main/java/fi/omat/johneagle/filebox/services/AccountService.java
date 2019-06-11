package fi.omat.johneagle.filebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.validationmodels.AccountModel;
import fi.omat.johneagle.filebox.repository.AccountRepository;

/**
 * Service class for registration and login functions.
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Finds users account by his username.
     *
     * @param username username of account.
     *
     * @return account found.
     */
    public Account findByUsername(String username) {
        return this.accountRepository.findByUsername(username);
    }

    /**
     * Finds users account by his nickname.
     *
     * @param nickname nickname of account.
     *
     * @return account found.
     */
    public Account findByNickname(String nickname) {
        return this.accountRepository.findByNickname(nickname);
    }

    /**
     * Creates a new account and saves it into database according to the data gotten in the model.
     *
     * @param account registration form model.
     */
    public void create(AccountModel account) {
        Account validated = new Account();
        validated.setUsername(account.getUsername());
        validated.setPassword(passwordEncoder.encode(account.getPassword()));
        validated.setNickname(account.getNickname());
        validated.setFirstName(account.getFirstName());
        validated.setLastName(account.getLastName());
        validated.getAuthorities().add("USER");

        this.accountRepository.save(validated);
    }

    /**
     * Checks if current userrs password equals given one.
     *
     * @param password password given.
     *
     * @return <code>true</code> if password is equivalent with the old one of current user and otherwise <code>false</code>.
     */
    public boolean equivalentPassword(String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());
        return user.getPassword().equals(passwordEncoder.encode(password));
    }
}
