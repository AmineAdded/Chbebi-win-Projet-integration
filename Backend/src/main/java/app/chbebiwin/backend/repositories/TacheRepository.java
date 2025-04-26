package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Tache.Tache;
import app.chbebiwin.backend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache,Long> {
    List<Tache> findAllByUser(Utilisateur user);
}
