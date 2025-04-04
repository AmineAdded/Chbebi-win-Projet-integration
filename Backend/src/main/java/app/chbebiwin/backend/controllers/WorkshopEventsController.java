package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.WorkshopEventsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkshopEventsController {
    private final WorkshopEventsService workshopEventsService;

    public WorkshopEventsController(WorkshopEventsService workshopEventsService) {
        this.workshopEventsService = workshopEventsService;
    }
}
