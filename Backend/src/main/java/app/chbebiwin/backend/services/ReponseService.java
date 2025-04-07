package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Question.Question;
import app.chbebiwin.backend.entities.Reponse.Reponse;
import app.chbebiwin.backend.entities.Reponse.ReponseRequest;
import app.chbebiwin.backend.repositories.QuestionRepository;
import app.chbebiwin.backend.repositories.ReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReponseService {
    private final ReponseRepository reponseRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ReponseService(ReponseRepository reponseRepository,QuestionRepository questionRepository) {
        this.reponseRepository = reponseRepository;
        this.questionRepository = questionRepository;
    }

    public Reponse createReponse(ReponseRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question non trouvé avec ID:"+request.getQuestionId()));
        Reponse reponse = new Reponse();
        reponse.setContenu(request.getContenu());
        reponse.setQuestion(question);
        return reponseRepository.save(reponse);
    }
    public String deleteReponse(Long id) {
        if(reponseRepository.existsById(id)){
            reponseRepository.deleteById(id);
            return "Réponse supprimé avec succès!";
        }
        return "Réponse non trouvée";
    }
    public Reponse getReponseById(Long id){
        if(reponseRepository.existsById(id)){
            return reponseRepository.findById(id).get();
        }
        return null;
    }
    public List<Reponse>getAllReponse(){
        return reponseRepository.findAll();
    }
    public Reponse updateReponse(Long id,ReponseRequest request) {
        return reponseRepository.findById(id).map(
                existingReponse -> {
                    Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(() -> new RuntimeException("Question non trouvé avec ID:"+request.getQuestionId()));
                    existingReponse.setContenu(request.getContenu());
                    existingReponse.setQuestion(question);
                    return reponseRepository.save(existingReponse);
                }).orElseGet(() -> {
            Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(() -> new RuntimeException("Question non trouvé avec ID:"+request.getQuestionId()));

            Reponse reponse = new Reponse();
            reponse.setContenu(request.getContenu());
            reponse.setQuestion(question);
            return reponseRepository.save(reponse);
        });
    }
    public String deleteAllReponses(){
        reponseRepository.deleteAll();
        return "Tous les réponses sont supprimés!";
    }

}
