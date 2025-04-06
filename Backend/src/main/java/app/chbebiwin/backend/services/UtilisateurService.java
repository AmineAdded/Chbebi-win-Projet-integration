package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
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

    /*public Utilisateur register(String nom,String email, String password,String confirmPassword) {
        if(nom != null && email != null && password != null && confirmPassword != null &&(password.equals(confirmPassword))){
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nom);
            utilisateur.setEmail(email);
            utilisateur.setMdpsCompte(password);
        }
    }*/
}
