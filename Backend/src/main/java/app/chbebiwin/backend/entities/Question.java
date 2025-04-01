package app.chbebiwin.backend.entities;

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
    @OneToMany(mappedBy = "question")
    private List<Reponse> reponses;

}
