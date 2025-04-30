package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Chapitre.Chapitre;
import app.chbebiwin.backend.entities.Chapitre.ChapitreRequest;
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

    @PostMapping("/create")
    public Chapitre addChapitre(@RequestBody ChapitreRequest chapitreRequest) {
        return  chapitreService.createChapitre(chapitreRequest);
    }
    @PutMapping("/update/{id}")
    public Chapitre updateChapitre(@PathVariable Long id,@RequestBody ChapitreRequest chapitreRequest) {
        return chapitreService.updateChapitre(id, chapitreRequest);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteChapitre(@PathVariable Long id) {

        return chapitreService.deleteChapitre(id);
    }
    @GetMapping("/getProgress/{id}")
   public long getProgress(@PathVariable Long id) {
        return chapitreService.getProgress(id);
    }
}
