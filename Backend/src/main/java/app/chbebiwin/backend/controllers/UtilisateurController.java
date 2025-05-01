package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Authentification.UpdateProfileRequest;
import app.chbebiwin.backend.entities.Authentification.UserReturn;
import app.chbebiwin.backend.entities.Authentification.loginRequest;
import app.chbebiwin.backend.entities.Authentification.signUpRequest;
import app.chbebiwin.backend.entities.Personnalite.PersonnaliteRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.services.UtilisateurService;
import org.springframework.http.ResponseEntity;
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
    public UserReturn login (@RequestBody loginRequest request){
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

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody UpdateProfileRequest request) {
        System.out.println("Nom reçu : " + request.getNom());
        try {
            Utilisateur updatedUser = utilisateurService.updateProfile(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/setPersonnalite")
    public Utilisateur setPersonnalite(@RequestBody PersonnaliteRequest request){
        return utilisateurService.setPersonnalite(request);
    }

}
