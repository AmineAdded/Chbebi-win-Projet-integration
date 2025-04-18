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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


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
    public Utilisateur getUserById(Long id){
        if(utilisateurRepository.existsById(id)){
            return utilisateurRepository.findById(id).get();
        }
        return null;
    }

    public Utilisateur loginUser(loginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getMdpsCompte())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        //Générer un token lors du login
        int tokenInt = 100000 + new Random().nextInt(900000);
        String token = String.valueOf(tokenInt);
        user.setAccessToken(token);
        utilisateurRepository.save(user);
        return user;
    }

    public Utilisateur registerUser(signUpRequest request) throws EmailAlreadyExistsException {
        if (request.getNom() != null &&
                request.getEmail() != null &&
                request.getPassword() != null &&
                request.getConfirmPassword() != null) {

            if (!request.getPassword().equals(request.getConfirmPassword()))
                throw new RuntimeException("كلمتا المرور غير متطابقتين");

            if (utilisateurRepository.findByEmail(request.getEmail()).isPresent())
                throw new EmailAlreadyExistsException("البريد الإلكتروني مُسجّل مسبقًا");

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(request.getNom());
            utilisateur.setEmail(request.getEmail());
            utilisateur.setMdpsCompte(passwordEncoder.encode(request.getPassword()));

            int tokenInt = 100000 + new Random().nextInt(900000);
            String token = String.valueOf(tokenInt);
            utilisateur.setAccessToken(token);

            utilisateurRepository.save(utilisateur);
            return utilisateur;
        }

        return null;
    }

    public String deleteAllUsers(){
        utilisateurRepository.deleteAll();
        return "Tous les utilisateurs sont supprimés!";
    }
}
