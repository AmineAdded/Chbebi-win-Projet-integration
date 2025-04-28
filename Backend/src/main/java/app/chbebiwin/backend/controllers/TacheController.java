package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Tache.Tache;
import app.chbebiwin.backend.entities.Tache.TacheRequest;
import app.chbebiwin.backend.services.TacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/Tache")
@CrossOrigin(origins="*")
public class TacheController {
    @Autowired
    private TacheService tacheService;

    @PostMapping("/create")
    public Tache createTache(@RequestBody TacheRequest tache){
        return tacheService.create(tache);
    }
    @PostMapping("/get/{id}")
    public List <Tache> get(@PathVariable Long id,@RequestBody LocalDate date){
        return tacheService.getTachesUser(id,date);
    }
    @PutMapping("/put/{id}")
    public Tache putTacheDone(@PathVariable Long id, @RequestBody Tache t){
        return tacheService.putTache(id,t);
    }
}
