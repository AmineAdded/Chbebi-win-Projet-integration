package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Chapitre.Chapitre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapitreRepository extends JpaRepository<Chapitre, Long> {
   Optional<List<Chapitre>> findAllByThematicId(long id);
}