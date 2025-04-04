package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.WorkshopEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkshopEventsRepository extends JpaRepository<WorkshopEvents, Long> {
}
