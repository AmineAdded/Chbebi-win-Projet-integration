package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.UserSousChapitreProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSousChapitreProgressRepository extends JpaRepository<UserSousChapitreProgress,Long> {
    UserSousChapitreProgress findByUtilisateurIdAndSousChapitreId(Long userId, Long sousChapitreId);
}
