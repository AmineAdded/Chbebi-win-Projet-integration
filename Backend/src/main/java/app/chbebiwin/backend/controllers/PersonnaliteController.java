package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Personnalite;
import app.chbebiwin.backend.services.PersonnaliteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Personnalite")
public class PersonnaliteController {
    private final PersonnaliteService personnaliteService;

    public PersonnaliteController(PersonnaliteService personnaliteService){
        this.personnaliteService = personnaliteService;
    }
    @PostMapping("/create")
    public Personnalite createPersonnalite(@RequestBody Personnalite personnalite) {
        return personnaliteService.createPersonnalite(personnalite);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return personnaliteService.deletePersonnalite(id);
    }
    @GetMapping("/get/{id}")
    public Personnalite get(@PathVariable Long id) {
        return personnaliteService.getPersonnaliteById(id);
    }
    @GetMapping("/getAll")
    public List<Personnalite> getAll() {
        return personnaliteService.getAllPersonnalites();
    }
    @PutMapping("/update/{id}")
    public Personnalite update(@PathVariable Long id, @RequestBody Personnalite personnalite) {
        return personnaliteService.updatePersonnalite(id,personnalite);
    }
}
