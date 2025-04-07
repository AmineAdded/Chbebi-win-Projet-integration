package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Question.Question;
import app.chbebiwin.backend.entities.Question.QuestionRequest;
import app.chbebiwin.backend.services.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/create")
    public Question create(@RequestBody QuestionRequest request) {
        return questionService.createQuestion(request);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return questionService.deleteQuestion(id);
    }
    @GetMapping("/get/{id}")
    public Question get(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }
    @GetMapping("/getAll")
    public List<Question> getAll() {
        return questionService.getAllQuestions();
    }
    @PutMapping("/update/{id}")
    public Question update(@PathVariable Long id, @RequestBody QuestionRequest request) {
        return questionService.updateQuestion(id,request);
    }
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return questionService.deleteAllQuestions();
    }
}
