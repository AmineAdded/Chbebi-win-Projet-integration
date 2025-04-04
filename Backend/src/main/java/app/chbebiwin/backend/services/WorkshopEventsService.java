package app.chbebiwin.backend.services;

import app.chbebiwin.backend.repositories.WorkshopEventsRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkshopEventsService {
    private final WorkshopEventsRepository workshopEventsRepository;

    public WorkshopEventsService(WorkshopEventsRepository workshopEventsRepository){
        this.workshopEventsRepository = workshopEventsRepository;
    }
}
