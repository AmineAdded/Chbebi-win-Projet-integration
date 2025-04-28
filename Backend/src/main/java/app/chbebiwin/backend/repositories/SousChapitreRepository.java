package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.SousChapitres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SousChapitreRepository extends JpaRepository<SousChapitres,Long> {
    List<SousChapitres> findAllByChapitreId(Long id);
}
