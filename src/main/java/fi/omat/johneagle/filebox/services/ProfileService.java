package fi.omat.johneagle.filebox.services;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.domain.models.SearchResult;
import fi.omat.johneagle.filebox.domain.validationmodels.PersonInfoModel;
import fi.omat.johneagle.filebox.repository.AccountRepository;
import fi.omat.johneagle.filebox.repository.ImageRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for registration and login functions.
 */
@Service
public class ProfileService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageRepository imageRepository;

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
     * Looks for people whose first and/or last name contains the text given and
     * returns a list of those found ones in more useful and safer form.
     *
     * @param search user typing
     *
     * @return list of models that contain found people.
     */
    public List<SearchResult> findPeopleWithParam(String search) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());

        // Parse the parameter in the actual parameter and get the result as raw data from database.
        String[] parts = search.split(" ");
        List<SearchResult> result = new ArrayList<>();
        List<Account> filtered;
        if (parts.length > 1) {
            StringBuilder firstName = new StringBuilder(parts[0]);
            String lastName = parts[parts.length - 1];

            for (int i = 1; i < parts.length - 1; i++) {
                firstName.append(" ").append(parts[i]);
            }

            filtered = this.accountRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName.toString(), lastName);
        } else {
            String name = parts[0];

            filtered = this.accountRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        }

        // Reformat the raw data into better format.
        for (Account found : filtered) {
            SearchResult model = new SearchResult();
            model.setNickname(found.getNickname());
            model.setName(found.getFullName());

            Image pic = found.getProfileImage();
            if (pic != null) {
                model.setPic(pic.getId());
            }

            result.add(model);
        }

        return result;
    }

    /**
     * Save image to database.
     *
     * @param file image file.
     * @param description content that is included to images data.
     */
    public void setProfilePicture(MultipartFile file, String description) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());

        this.imageRepository.deleteByOwner(user);

        Image image = new Image();

        image.setFilename(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setContentLength(file.getSize());

        try {
            image.setContent(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        image.setTimestamp(LocalDateTime.now());
        image.setDescription(description);
        image.setOwner(user);

        this.imageRepository.save(image);
    }

    /**
     * Updates the profile info of the current user.
     *
     * @param validationModel model that validated the data gotten.
     */
    public void updateProfile(PersonInfoModel validationModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());

        try {
            BeanUtils.copyProperties(user, validationModel);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Password updated only if given new one.
        if (!validationModel.getNewPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(validationModel.getNewPassword()));
        }

        this.accountRepository.save(user);
    }
}
