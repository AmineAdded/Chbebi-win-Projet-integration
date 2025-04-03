package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.FeedbackService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
}
