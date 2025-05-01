package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSousChapitreProgressRepository extends JpaRepository<UserSousChapitreProgress,Long> {
    UserSousChapitreProgress findByUtilisateurIdAndSousChapitreId(Long userId, Long sousChapitreId);
    List<UserSousChapitreProgress> findAllByUtilisateurIdAndSousChapitreIdIn(Long utilisateurId, List<Long> sousChapitreIds);
}
