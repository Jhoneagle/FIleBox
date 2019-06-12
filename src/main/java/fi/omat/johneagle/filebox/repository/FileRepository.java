package fi.omat.johneagle.filebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.File;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;

/**
 * Database table interface for JPA to file table.
 */
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByVisibilityInAndOwner(Collection<FileVisibility> visibility, Account owner);
}
