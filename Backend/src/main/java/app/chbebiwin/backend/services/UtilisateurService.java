package app.chbebiwin.backend.services;

import app.chbebiwin.backend.Config.PasswordEncoder;
import app.chbebiwin.backend.entities.Authentification.loginRequest;
import app.chbebiwin.backend.entities.Authentification.signUpRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UtilisateurService {
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur createUtilisateur(Utilisateur user) {
        return utilisateurRepository.save(user);
    }
    public String deleteUtilisateur(Long id) {
        if(utilisateurRepository.existsById(id)){
            utilisateurRepository.deleteById(id);
            return "Utilisateur supprimé avec succès!";
        }
        return "Utilisateur non touvé!";
    }
    public List<Utilisateur> getAllUsers () {
        return utilisateurRepository.findAll();
    }

    public Utilisateur loginUser(loginRequest request){
        return utilisateurRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé!"));
    }

    public Utilisateur registerUser(signUpRequest request) {
        if(request.getNom()!=null && request.getEmail() != null && request.getPassword() != null && request.getConfirmPassword() != null &&(request.getPassword().equals(request.getConfirmPassword()))){
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMdpsCompte(passwordEncoder.encode(request.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }
        return null;
    }
}
