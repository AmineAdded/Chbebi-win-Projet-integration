package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgress;
import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgressRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.SousChapitreRepository;
import app.chbebiwin.backend.repositories.UserSousChapitreProgressRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSousChapitreProgressService {
    @Autowired
    private UserSousChapitreProgressRepository userSousChapitreProgressRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private SousChapitreRepository sousChapitreRepository;

    public UserSousChapitreProgress getLastReadPage( UserSousChapitreProgressRequest request) {
        return userSousChapitreProgressRepository.findByUtilisateurIdAndSousChapitreId(request.getUserId(), request.getSousChapitreId());
    }
    public UserSousChapitreProgress setLastPageRead(UserSousChapitreProgressRequest request) {
        UserSousChapitreProgress u = userSousChapitreProgressRepository.findByUtilisateurIdAndSousChapitreId(request.getUserId(),request.getSousChapitreId());
        u.setLastPageRead(request.getLastPageRead());
        u.setPourcentage(request.getPourcentage());
        userSousChapitreProgressRepository.save(u);

        return u;
    }
    public List<UserSousChapitreProgress> createUserSousChapitreProgressIfNotExists(Long userId, List<Long> sousChapitreIds) {
        Utilisateur u = utilisateurRepository.findById(userId).get();

        for (Long id : sousChapitreIds) {
            SousChapitres s = sousChapitreRepository.findById(id).get();

            // Vérifier si la ligne existe déjà
            UserSousChapitreProgress existing = userSousChapitreProgressRepository
                    .findByUtilisateurIdAndSousChapitreId(userId, id);

            // Si non trouvée, on la crée
            if (existing == null) {
                UserSousChapitreProgress progress = new UserSousChapitreProgress();
                progress.setUtilisateur(u);
                progress.setSousChapitre(s);
                progress.setPourcentage(0); // valeur initiale
                progress.setLastPageRead(1);
                userSousChapitreProgressRepository.save(progress);
            }
        }

        // Retourner la liste complète après insertion des manquants
        return userSousChapitreProgressRepository.findAllByUtilisateurIdAndSousChapitreIdIn(userId, sousChapitreIds);
    }



}
