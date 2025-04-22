package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.WorkshopEvents;
import app.chbebiwin.backend.services.WorkshopEventsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/WorkshopEvents")
@CrossOrigin(origins = "*")
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
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return workshopEventsService.deleteAllWorkshopEvents();
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + filename);
        Files.write(path, file.getBytes());
        return filename; // ou un lien complet genre "http://localhost:9090/uploads/" + filename
    }

    @DeleteMapping("/deleteExpired")
    public String deleteExpired() {
        return workshopEventsService.deletePastWorkshopEvents();
    }
}
