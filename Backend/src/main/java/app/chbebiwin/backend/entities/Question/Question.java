package app.chbebiwin.backend.entities.Question;

import app.chbebiwin.backend.entities.Reponse.Reponse;
import app.chbebiwin.backend.entities.Test;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;
    @ManyToOne
    //clé etrangére
    @JoinColumn(name = "test_id")
    @JsonIgnore
    private Test test;
    //question : représente l'attribut question dans la classe Reponse
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Reponse> reponses;

}
