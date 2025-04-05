package app.chbebiwin.backend.entities.Question;

import app.chbebiwin.backend.entities.Reponse.Reponse;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    private String contenu;
    private Long testId;
    @OneToMany(mappedBy = "question")
    private List<Reponse> reponses;
}
