package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public String generateResetToken(Utilisateur utilisateur) {
        // Générer un token aléatoire (UUID)
        String token = UUID.randomUUID().toString();

        // Définir la date d'expiration (ex : 15 minutes)
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        // Mettre à jour l'utilisateur
        utilisateur.setResetToken(token);
        utilisateur.setTokenExpiry(expiry);

        // Sauvegarder dans la base de données
        utilisateurRepository.save(utilisateur);

        return token;
    }

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