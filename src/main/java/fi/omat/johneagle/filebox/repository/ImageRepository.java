package fi.omat.johneagle.filebox.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Image;
import fi.omat.johneagle.filebox.domain.enums.PictureState;

/**
 * Image database table interface for JPA.
 */
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
    @EntityGraph(attributePaths = {"owner", "content"})
    List<Image> findAllByPictureStateAndOwnerIn(PictureState pictureState, Collection<Account> owner);

    Image findByOwnerAndPictureState(Account owner, PictureState pictureState);
    List<Image> findAllByOwner(Account owner);
}
