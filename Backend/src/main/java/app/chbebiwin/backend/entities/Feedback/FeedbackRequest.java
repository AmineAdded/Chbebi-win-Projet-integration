package app.chbebiwin.backend.entities.Feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    private String message;
    private Long utilisateurId;
    private String nom;
}
