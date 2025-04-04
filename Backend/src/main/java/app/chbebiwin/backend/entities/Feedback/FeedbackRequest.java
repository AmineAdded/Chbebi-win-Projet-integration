package app.chbebiwin.backend.entities.Feedback;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String message;
    private Long utilisateurId;
}
