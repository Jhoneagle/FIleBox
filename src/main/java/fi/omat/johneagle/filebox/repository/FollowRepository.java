package fi.omat.johneagle.filebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import fi.omat.johneagle.filebox.domain.entities.Account;
import fi.omat.johneagle.filebox.domain.entities.Follow;

/**
 * Account database table interface for JPA.
 */
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowedAndFollower(Account followed, Account follower);
    Long countByFollowed(Account followed);
    List<Follow> findAllByFollowed(Account followed);
}
