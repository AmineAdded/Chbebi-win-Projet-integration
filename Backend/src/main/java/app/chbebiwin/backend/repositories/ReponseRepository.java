package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Reponse.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
}
