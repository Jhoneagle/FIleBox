package fi.omat.johneagle.filebox.repository;

import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Image;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Image database table interface for JPA.
 */
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
    @EntityGraph(attributePaths = {"owner"})
    List<Image> findAllByOwnerIn(Collection<Account> owner);

    Image findByOwner(Account owner);
    List<Image> findAllByOwner(Account owner);

    void deleteByOwner(Account user);
}
