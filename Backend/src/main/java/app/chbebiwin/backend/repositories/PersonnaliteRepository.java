package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Personnalite.Personnalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnaliteRepository extends JpaRepository<Personnalite, Long> {
}
