package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.AgendaPage.AgendaPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaPageRepository extends JpaRepository<AgendaPage, Long> {
    List<AgendaPage> findByUtilisateurIdOrderByPageNumber(Long id);
}
