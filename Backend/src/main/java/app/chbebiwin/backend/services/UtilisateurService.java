package app.chbebiwin.backend.services;

import app.chbebiwin.backend.Exceptions.EmailAlreadyExistsException;
import app.chbebiwin.backend.entities.Authentification.UpdateProfileRequest;
import app.chbebiwin.backend.entities.Authentification.loginRequest;
import app.chbebiwin.backend.entities.Authentification.signUpRequest;
import app.chbebiwin.backend.entities.Personnalite.Personnalite;
import app.chbebiwin.backend.entities.Personnalite.PersonnaliteRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.PersonnaliteRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PersonnaliteRepository personnaliteRepository;


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

            //Envoi d'un email lors du Sign Up
            Context context = new Context();
            context.setVariable("name", utilisateur.getNom());
            String htmlContent = templateEngine.process("email/SignUp", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
                helper.setTo(utilisateur.getEmail());
                helper.setSubject("مرحبا بيك في Chbebi Win ");
                helper.setText(htmlContent, true); // true = HTML

                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException("حدث خطأ أثناء إرسال البريد الإلكتروني", e);
            }
            return utilisateur;
        }

        return null;
    }

    public String deleteAllUsers(){
        utilisateurRepository.deleteAll();
        return "Tous les utilisateurs sont supprimés!";
    }

    public Utilisateur updateProfile(Long id, UpdateProfileRequest request) {
        Optional<Utilisateur> optionalUser = utilisateurRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        Utilisateur user = optionalUser.get();

        if (!user.getEmail().equals(request.getEmail()) &&
                utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("البريد الإلكتروني مُستخدم بالفعل");
        }

        if (request.getCurrentPassword() != null && !request.getCurrentPassword().isEmpty()) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getMdpsCompte())) {
                throw new RuntimeException("كلمة المرور الحالية غير صحيحة");
            }

            if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                user.setMdpsCompte(passwordEncoder.encode(request.getNewPassword()));
            }
        }

        user.setNom(request.getNom());
        user.setEmail(request.getEmail());

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        utilisateurRepository.save(user);
        return user;
    }

    public Utilisateur setPersonnalite(PersonnaliteRequest request){
        Utilisateur u = utilisateurRepository.findById(request.getId()).get();
        Personnalite p = personnaliteRepository.findById(request.getType()).get();
        u.setPersonnalite(p);
        utilisateurRepository.save(u);
        return u;
    }
}
