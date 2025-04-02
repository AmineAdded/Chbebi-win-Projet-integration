package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Critere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CritereRepository extends JpaRepository<Critere,Long> {
}
