package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Chapitre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapitreRepository extends JpaRepository<Chapitre, Long> {
    List<Chapitre> findByIsSuperChapitreTrue();
    List<Chapitre> findByIsSuperChapitreFalse();
    List<Chapitre> findBySuperChapitreId(Long superChapitreId);
    List<Chapitre> findByIsSuperChapitreTrueAndThematicId(Long thematicId);

}