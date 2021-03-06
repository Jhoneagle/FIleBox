package fi.omat.johneagle.filebox.services;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.File;
import fi.omat.johneagle.filebox.domain.entities.Follow;
import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import fi.omat.johneagle.filebox.domain.models.FileModel;
import fi.omat.johneagle.filebox.domain.models.FollowModel;
import fi.omat.johneagle.filebox.domain.models.SearchResult;
import fi.omat.johneagle.filebox.domain.validationmodels.ChangePasswordModel;
import fi.omat.johneagle.filebox.domain.validationmodels.DownloadFile;
import fi.omat.johneagle.filebox.domain.validationmodels.PersonInfoModel;
import fi.omat.johneagle.filebox.repository.AccountRepository;
import fi.omat.johneagle.filebox.repository.FileRepository;
import fi.omat.johneagle.filebox.repository.FollowRepository;
import fi.omat.johneagle.filebox.repository.ImageRepository;

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

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FollowRepository followRepository;

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

        // Reformat the raw data into better format and return it.
        return filtered.stream().map(this::createSearchResult).collect(Collectors.toList());
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

        this.accountRepository.save(user);
    }

    /**
     * Updates users password by the given info.
     *
     * @param changePasswordModel validated new password.
     */
    public void updatePassword(ChangePasswordModel changePasswordModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());

        user.setPassword(passwordEncoder.encode(changePasswordModel.getNewPassword()));
        this.accountRepository.save(user);
    }

    /**
     * Get all files allowed to see.
     *
     * @param nickname whose files wanted to be seen
     *
     * @return list of files can be seen from user.
     */
    public List<FileModel> getShowableFiles(String nickname) {
        List<FileVisibility> access = new ArrayList<>();
        access.add(FileVisibility.EVERYONE);
        Account whoseWall = findByNickname(nickname);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedIn = findByUsername(auth.getName());

        Follow following = this.followRepository.findByFollowedAndFollower(whoseWall, loggedIn);

        if (following != null || loggedIn.equals(whoseWall)) {
            access.add(FileVisibility.FOLLOWERS);
        }

        if (loggedIn.equals(whoseWall)) {
            access.add(FileVisibility.ME);
        }

        List<File> files = this.fileRepository.findAllByVisibilityInAndOwner(access, whoseWall);

        List<FileModel> toView = new ArrayList<>();
        files.forEach(file -> {
            FileModel model = new FileModel();
            model.setId(file.getId());
            model.setContentLength(readableFileSize(file.getContentLength()));
            model.setContentType(file.getContentType());
            model.setFilename(file.getFilename());
            model.setTimestamp(file.getTimestamp());

            toView.add(model);
        });

        return toView;
    }

    /**
     * Formats file size to readable string.
     *
     * @param size files size in bytes.
     *
     * @return bytes in pretty string.
     */
    private String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void deleteFile(Long id) {
        this.fileRepository.delete(this.fileRepository.getOne(id));
    }

    /**
     * Saves the new file that is added.
     *
     * @param download new file that has been validated.
     */
    public void saveFile(DownloadFile download) {
        MultipartFile newOne = download.getFile();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = findByUsername(auth.getName());

        File file = new File();
        file.setFilename(newOne.getOriginalFilename());
        file.setContentType(newOne.getContentType());
        file.setContentLength(newOne.getSize());

        try {
            file.setContent(newOne.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        file.setTimestamp(LocalDateTime.now());
        file.setOwner(user);
        file.setVisibility(download.getFileVisibility());

        this.fileRepository.save(file);
    }

    /**
     * Get follows and boolean of is followed by user.
     *
     * @param whoseWall person been checked.
     *
     * @return info if user follows the person and how many people follows him.
     */
    public FollowModel getFollowing(String whoseWall) {
        Account wall = findByNickname(whoseWall);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedIn = findByUsername(auth.getName());

        Follow follow = this.followRepository.findByFollowedAndFollower(wall, loggedIn);
        FollowModel model = new FollowModel();

        model.setFollowed(follow != null && !loggedIn.getNickname().equals(wall.getNickname()));
        model.setFollows(this.followRepository.countByFollowed(wall));

        return model;
    }

    /**
     * Follows the person if not yet otherwise removes the follow.
     *
     * @param whoseWall person who user wants to follow or unfollow.
     */
    public void follow(String whoseWall) {
        Account wall = findByNickname(whoseWall);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedIn = findByUsername(auth.getName());

        if (loggedIn.getNickname().equals(wall.getNickname())) {
            return;
        }

        Follow follow = this.followRepository.findByFollowedAndFollower(wall, loggedIn);

        if (follow == null) {
            Follow newOne = new Follow();
            newOne.setFollowed(wall);
            newOne.setFollower(loggedIn);
            newOne.setTimestamp(LocalDateTime.now());

            this.followRepository.save(newOne);
        } else {
            this.followRepository.delete(follow);
        }
    }

    public List<SearchResult> getFollowers(String nickname) {
        List<Follow> allByFollowed = this.followRepository.findAllByFollowed(findByNickname(nickname));
        return allByFollowed.stream().map(Follow::getFollower).map(this::createSearchResult).collect(Collectors.toList());
    }

    private SearchResult createSearchResult(Account follower) {
        SearchResult model = new SearchResult();
        model.setNickname(follower.getNickname());
        model.setName(follower.getFullName());
        Image pic = follower.getProfileImage();

        if (pic != null) {
            model.setPic(pic.getId());
        }

        return model;
    }
}
