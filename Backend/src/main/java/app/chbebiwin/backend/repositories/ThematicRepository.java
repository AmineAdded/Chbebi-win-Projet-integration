package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Thematic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThematicRepository extends JpaRepository<Thematic, Long> {
}