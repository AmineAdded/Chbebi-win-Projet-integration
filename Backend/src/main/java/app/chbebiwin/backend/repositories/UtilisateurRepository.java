package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    Utilisateur findByEmail(String email);
    Utilisateur findByResetToken(String token);
}
