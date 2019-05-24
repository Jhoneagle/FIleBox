package fi.omat.johneagle.filebox.repository;

import fi.omat.johneagle.filebox.domain.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database table interface for JPA to file table.
 */
public interface FileRepository extends JpaRepository<File, Long> {

}
