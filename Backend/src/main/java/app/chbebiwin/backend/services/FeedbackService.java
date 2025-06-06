package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Feedback.Feedback;
import app.chbebiwin.backend.entities.Feedback.FeedbackRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.FeedbackRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, UtilisateurRepository utilisateurRepository) {
        this.feedbackRepository = feedbackRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Méthode avec DTO pour la création
    public Feedback createFeedback(FeedbackRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new RuntimeException("التعليق لا يجب أن يكون فارغًا");
        }

        Feedback feedback = new Feedback();
        feedback.setMessage(request.getMessage());

        // Vérifie si un utilisateur est associé
        if (request.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID: " + request.getUtilisateurId()));
            feedback.setUtilisateur(utilisateur);
        }

        return feedbackRepository.save(feedback);
    }


    public String deleteFeedback(long id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return "Feedback supprimé avec succès!";
        }
        return "Feedback non trouvé!";
    }

    public Feedback getFeedbackById(long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

//    public List<Feedback> getAllFeedback() {
//        return feedbackRepository.findAll();
//    }
public List<FeedbackRequest> getAllFeedback() {
    List<Feedback> feedbackList = feedbackRepository.findAll();
    List<FeedbackRequest> feedbackDTOList = new ArrayList<>();

    for (Feedback feedback : feedbackList) {
        Utilisateur utilisateur = feedback.getUtilisateur();

        Long utilisateurId = null;
        String userName = "مجهول";

        if (utilisateur != null) {
            utilisateurId = utilisateur.getId();
            userName = utilisateur.getNom();
        }

        feedbackDTOList.add(new FeedbackRequest(feedback.getMessage(), utilisateurId, userName));
    }

    return feedbackDTOList;
}



    public Feedback updateFeedback(long id, FeedbackRequest request) {
        return feedbackRepository.findById(id)
                .map(existingFeedback -> {
                    existingFeedback.setMessage(request.getMessage());
                    Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID: "+request.getUtilisateurId()));
                    existingFeedback.setUtilisateur(utilisateur);
                    return feedbackRepository.save(existingFeedback);
                })
                .orElseGet(() -> {
                    Feedback newFeedback = new Feedback();
                    newFeedback.setMessage(request.getMessage());
                    newFeedback.setUtilisateur(utilisateurRepository.findById(request.getUtilisateurId())
                            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID: " + request.getUtilisateurId())));
                    return feedbackRepository.save(newFeedback);
                });
    }
    public String deleteAllFeedback(){
        feedbackRepository.deleteAll();
        return "Tous les feedback sont supprimés!";
    }

}
