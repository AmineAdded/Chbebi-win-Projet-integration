package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.services.UtilisateurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Utilisateur")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping ("/create")
    public Utilisateur create(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.createUtilisateur(utilisateur);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return utilisateurService.deleteUtilisateur(id);
    }
    @GetMapping("/getAll")
    public List<Utilisateur> getAll (){
        return utilisateurService.getAllUsers();
    }
}
