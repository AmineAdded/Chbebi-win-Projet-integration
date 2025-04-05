package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Reponse.Reponse;
import app.chbebiwin.backend.entities.Reponse.ReponseRequest;
import app.chbebiwin.backend.services.ReponseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Reponse")
public class ReponseController {
    private final ReponseService reponseService;

    public ReponseController(ReponseService reponseService) {
        this.reponseService = reponseService;
    }

    @PostMapping("/create")
    public Reponse create(@RequestBody ReponseRequest request) {
        return reponseService.createReponse(request);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return reponseService.deleteReponse(id);
    }
    @GetMapping("/get/{id}")
    public Reponse get(@PathVariable Long id) {
        return reponseService.getReponseById(id);
    }
    @GetMapping("/getAll")
    public List<Reponse> getAll() {
        return reponseService.getAllReponse();
    }
    @PutMapping("/update/{id}")
    public Reponse update(@PathVariable Long id, @RequestBody ReponseRequest request) {
        return reponseService.updateReponse(id, request);
    }
}
