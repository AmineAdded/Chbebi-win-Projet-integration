package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Chapitre;
import app.chbebiwin.backend.services.ChapitreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapitres")
@CrossOrigin(origins ="*")
public class ChapitreController {
    @Autowired
    private ChapitreService chapitreService;

    @GetMapping("/get/{id}")
    public List<Chapitre> getChapitresByThematic(@PathVariable Long id) {
        return chapitreService.getChapitres(id);
    }

    @PostMapping("/craete")
    public Chapitre addChapitre(@RequestBody Chapitre chapitre) {
        return  chapitreService.createChapitre(chapitre);
    }
    @PutMapping("/upadte/{id}")
    public Chapitre updateChapitre(@PathVariable Long id,@RequestBody Chapitre chapitre) {
        return chapitreService.updateChapitre(id, chapitre);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteChapitre(@PathVariable Long id) {
        return chapitreService.deleteChapitre(id);
    }
}
