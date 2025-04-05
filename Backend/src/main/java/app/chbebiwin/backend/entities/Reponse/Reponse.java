package app.chbebiwin.backend.entities.Reponse;

import app.chbebiwin.backend.entities.Question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;
    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;
}
