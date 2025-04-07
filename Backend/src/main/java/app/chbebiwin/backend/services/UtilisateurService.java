package app.chbebiwin.backend.services;

import app.chbebiwin.backend.Exceptions.EmailAlreadyExistsException;
import app.chbebiwin.backend.entities.Authentification.loginRequest;
import app.chbebiwin.backend.entities.Authentification.signUpRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        return utilisateurRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public Utilisateur registerUser(signUpRequest request) throws EmailAlreadyExistsException {
        if(request.getNom()!=null && request.getEmail() != null && request.getPassword() != null && request.getConfirmPassword() != null &&(request.getPassword().equals(request.getConfirmPassword()))){
        Utilisateur utilisateur = new Utilisateur();
            if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) throw new EmailAlreadyExistsException("Email is already taken");

            utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMdpsCompte(passwordEncoder.encode(request.getPassword()));
        return utilisateurRepository.save(utilisateur);
        }
        return null;
    }
    public String deleteAllUsers(){
        utilisateurRepository.deleteAll();
        return "Tous les utilisateurs sont supprimés!";
    }
}
