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

    public Utilisateur create(Utilisateur user) {
        return utilisateurRepository.save(user);
    }
    public List<Utilisateur> getUsers () {
        return utilisateurRepository.findAll();
    }
}
