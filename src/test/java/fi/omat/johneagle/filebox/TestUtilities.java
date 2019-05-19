package fi.omat.johneagle.filebox;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Image;

import java.time.LocalDateTime;

public class TestUtilities {
    public static Account createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return account;
    }

    public static Account createFullAccount(String username, String password, String firstName, String lastName) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setNickname(firstName + lastName);
        account.getAuthorities().add("USER");
        return account;
    }

    public static Image createImage(String filename, String description, String contentType, LocalDateTime time) {
        Image image = new Image();
        image.setTimestamp(time);
        image.setContentType(contentType);
        image.setDescription(description);
        image.setFilename(filename);
        image.setName(filename);
        return image;
    }
}
