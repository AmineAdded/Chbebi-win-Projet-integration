package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Question.Question;
import app.chbebiwin.backend.entities.Question.QuestionRequest;
import app.chbebiwin.backend.entities.Test;
import app.chbebiwin.backend.repositories.QuestionRepository;
import app.chbebiwin.backend.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    @Autowired
    public QuestionService(QuestionRepository questionRepository, TestRepository testRepository) {
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
    }

    public Question createQuestion(QuestionRequest request){
        Test test = testRepository.findById(request.getTestId()).orElseThrow(() -> new RuntimeException("teste non trouvé avec ID:"+request.getTestId()));

        Question question = new Question();
        question.setContenu(request.getContenu());
        question.setTest(test);

        return questionRepository.save(question);
    }
    public String deleteQuestion(Long id){
        if(questionRepository.existsById(id)){
            questionRepository.deleteById(id);
            return "Question supprimé avec succès!";
        }
        return "Question non trouvé!";
    }
    public Question getQuestionById(Long id){
        if(questionRepository.existsById(id)){
            return questionRepository.findById(id).get();
        }
        return null;
    }
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }
    public Question updateQuestion(Long id, QuestionRequest request) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setContenu(request.getContenu());
                    Test test = testRepository.findById(request.getTestId())
                            .orElseThrow(() -> new RuntimeException("Test introuvable avec ID: " + request.getTestId()));
                    existingQuestion.setTest(test);
                    return questionRepository.save(existingQuestion);
                })
                .orElseGet(() -> {
                    Test test = testRepository.findById(request.getTestId())
                            .orElseThrow(() -> new RuntimeException("Test introuvable avec ID: " + request.getTestId()));
                    Question newQuestion = new Question();
                    newQuestion.setContenu(request.getContenu());
                    newQuestion.setTest(test);
                    return questionRepository.save(newQuestion);
                });
    }

}
