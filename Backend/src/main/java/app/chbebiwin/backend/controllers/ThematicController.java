package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Thematic;
import app.chbebiwin.backend.services.ThematicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thematics")
@CrossOrigin(origins = "*")
public class ThematicController {

    private final ThematicService thematicService;

    public ThematicController(ThematicService thematicService) {
        this.thematicService = thematicService;
    }

    @GetMapping
    public List<Thematic> getAllThematics() {
        return thematicService.getAllThematics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Thematic> getThematicById(@PathVariable Long id) {
        return thematicService.getThematicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Thematic createThematic(@RequestBody Thematic thematic) {
        return thematicService.createThematic(thematic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Thematic> updateThematic(@PathVariable Long id, @RequestBody Thematic thematic) {
        return ResponseEntity.ok(thematicService.updateThematic(id, thematic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThematic(@PathVariable Long id) {
        thematicService.deleteThematic(id);
        return ResponseEntity.noContent().build();
    }
}