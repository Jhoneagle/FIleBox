package fi.omat.johneagle.filebox.repository;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.File;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Database table interface for JPA to file table.
 */
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByVisibilityInAndOwner(Collection<FileVisibility> visibility, Account owner);
    List<File> findAllBySpecificCanSeeContainsAndOwner(List<String> specificCanSee, Account owner);
}
