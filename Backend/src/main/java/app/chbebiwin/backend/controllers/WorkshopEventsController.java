package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.WorkshopEvents;
import app.chbebiwin.backend.services.WorkshopEventsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/WorkshopEvents")
public class WorkshopEventsController {
    private final WorkshopEventsService workshopEventsService;

    public WorkshopEventsController(WorkshopEventsService workshopEventsService) {
        this.workshopEventsService = workshopEventsService;
    }
    @PostMapping("/create")
    public WorkshopEvents create(@RequestBody WorkshopEvents workshopEvents) {
        return workshopEventsService.createWorkshopEvents(workshopEvents);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return workshopEventsService.deleteWorkshopEvents(id);
    }
    @GetMapping("/get/{id}")
    public WorkshopEvents get(@PathVariable Long id) {
        return workshopEventsService.getWorkshopEventsById(id);
    }
    @GetMapping("/getAll")
    public List<WorkshopEvents> getAll() {
        return workshopEventsService.getAllWorkshopEvents();
    }
    @PutMapping("/update/{id}")
    public WorkshopEvents update(@PathVariable Long id, @RequestBody WorkshopEvents workshopEvents) {
        return workshopEventsService.updateWorkshopEvents(id,workshopEvents);
    }
}
