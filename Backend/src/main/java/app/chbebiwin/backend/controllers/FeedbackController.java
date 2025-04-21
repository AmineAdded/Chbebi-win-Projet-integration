package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Feedback.Feedback;
import app.chbebiwin.backend.entities.Feedback.FeedbackRequest;
import app.chbebiwin.backend.services.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/create")
    public Feedback create(@RequestBody FeedbackRequest request) {
        return feedbackService.createFeedback(request);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        return feedbackService.deleteFeedback(id);
    }
    @GetMapping("/get/{id}")
    public Feedback get(@PathVariable long id) {
        return feedbackService.getFeedbackById(id);
    }
    @GetMapping("/getAll")
    public List<FeedbackRequest> getAll() {
        return feedbackService.getAllFeedback();
    }
    @PutMapping("/update/{id}")
    public Feedback update(@PathVariable long id, @RequestBody FeedbackRequest request) {
        return feedbackService.updateFeedback(id,request);
    }
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return feedbackService.deleteAllFeedback();
    }
}
