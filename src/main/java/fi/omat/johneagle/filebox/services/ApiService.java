package fi.omat.johneagle.filebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.File;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import fi.omat.johneagle.filebox.domain.jsonmodels.VisibilityUpdate;
import fi.omat.johneagle.filebox.repository.AccountRepository;
import fi.omat.johneagle.filebox.repository.FileRepository;

/**
 * Service class for API end points.
 */
@Service
public class ApiService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AccountRepository accountRepository;

    public File getFile(Long id) {
        return this.fileRepository.getOne(id);
    }

    public boolean updateFIleVisibility(VisibilityUpdate update) {
        FileVisibility fileVisibility = update.getFileVisibility();
        Long id = update.getId();

        try {
            File one = this.fileRepository.getOne(id);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account user = accountRepository.findByUsername(auth.getName());

            if (!one.getOwner().equals(user)) {
                throw new RuntimeException("User doesn't own the file!");
            }

            one.setVisibility(fileVisibility);
            this.fileRepository.save(one);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
