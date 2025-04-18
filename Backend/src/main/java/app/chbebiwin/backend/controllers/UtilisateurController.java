package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Authentification.loginRequest;
import app.chbebiwin.backend.entities.Authentification.signUpRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.services.UtilisateurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Utilisateur")
@CrossOrigin(origins = "*")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return utilisateurService.deleteUtilisateur(id);
    }
    @GetMapping("/getAll")
    public List<Utilisateur> getAll (){
        return utilisateurService.getAllUsers();
    }
    @GetMapping("/get/{id}")
    public Utilisateur get(@PathVariable Long id){
        return utilisateurService.getUserById(id);
    }

    @PostMapping("/login")
    public Utilisateur login (@RequestBody loginRequest request){
        return utilisateurService.loginUser(request);
    }

    @PostMapping("/register")
    public Utilisateur register(@RequestBody signUpRequest request) {
        return utilisateurService.registerUser(request);
    }

    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return utilisateurService.deleteAllUsers();
    }
}
