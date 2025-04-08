package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

public void sendResetPasswordEmail(String to, String token) {
    Context context = new Context();
    context.setVariable("token", token);
    context.setVariable("link", "http://localhost:8081/reset-password");

    String htmlContent = templateEngine.process("email/reset-password", context);

    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Réinitialisation de mot de passe");
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(mimeMessage);
    } catch (MessagingException e) {
        throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
    }
}
    public String generateResetToken(Utilisateur utilisateur) {
        // Générer un token aléatoire de 6 chiffres
        int tokenInt = 100000 + new Random().nextInt(900000); // Entre 100000 et 999999
        String token = String.valueOf(tokenInt);

        // Définir la date d'expiration (ex : 15 minutes)
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        // Mettre à jour l'utilisateur
        utilisateur.setResetToken(token);
        utilisateur.setTokenExpiry(expiry);

        // Sauvegarder dans la base de données
        utilisateurRepository.save(utilisateur);

        return token;
    }
}
