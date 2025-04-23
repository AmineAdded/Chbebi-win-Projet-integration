package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.AgendaPage;
import app.chbebiwin.backend.services.AgendaPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaPageController {

    @Autowired
    private AgendaPageService service;

    @GetMapping("/{userId}")
    public List<AgendaPage> getAllPages(@PathVariable String userId) {
        return service.getAllPages(userId);
    }

    @PostMapping("/save")
    public AgendaPage savePage(@RequestBody AgendaPage page) {
        return service.savePage(page);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePage(@PathVariable Long id) {
        service.deletePage(id);
    }
}