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

    @PostMapping ("/createUtilisateur")
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.create(utilisateur);
    }
    @GetMapping("/getUsers")
    public List<Utilisateur> getUsers (){
        return utilisateurService.getUsers();
    }
}
