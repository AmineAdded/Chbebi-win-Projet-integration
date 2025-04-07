package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Critere.Critere;
import app.chbebiwin.backend.entities.Critere.CritereRequest;
import app.chbebiwin.backend.services.CritereService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Critere")
public class CritereController {
    private final CritereService critereService;

    public CritereController(CritereService critereService) {
        this.critereService = critereService;
    }

    @PostMapping("/create")
    public Critere create(@RequestBody CritereRequest request) {
        return critereService.createCritere(request);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return critereService.deleteCritere(id);
    }
    @GetMapping("/get/{id}")
    public Critere get(@PathVariable Long id) {
        return critereService.getCritereById(id);
    }
    @GetMapping("/getAll")
    public List<Critere> getAll() {
        return critereService.getAllCritere();
    }
    @PutMapping("/update/{id}")
    public Critere update(@PathVariable Long id, @RequestBody CritereRequest request) {
        return critereService.updateCritere(id,request);
    }
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return critereService.deleteAllCriteres();
    }
}
