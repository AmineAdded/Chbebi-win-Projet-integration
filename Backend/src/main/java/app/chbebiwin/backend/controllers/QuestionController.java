package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.QuestionService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
}
