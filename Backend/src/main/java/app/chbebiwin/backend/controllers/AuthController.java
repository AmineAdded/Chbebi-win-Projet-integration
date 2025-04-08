package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import app.chbebiwin.backend.services.EmailService;
import app.chbebiwin.backend.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
        }

        Utilisateur utilisateur = user.get();
        String token = emailService.generateResetToken(utilisateur);

        emailService.sendResetPasswordEmail(email,token);

        return ResponseEntity.ok("Email envoyé !");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        String confirmPassword = body.get("confirmPassword");
        String token = body.get("token");

        Utilisateur user = utilisateurRepository.findByResetToken(token);
        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide ou expiré.");
        }
        if(!password.equals(confirmPassword)) {{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les mots de passe ne correspondent pas.");}
        }
        user.setMdpsCompte(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        utilisateurRepository.save(user);

        return ResponseEntity.ok("✅ Mot de passe réinitialisé.");
    }
}
