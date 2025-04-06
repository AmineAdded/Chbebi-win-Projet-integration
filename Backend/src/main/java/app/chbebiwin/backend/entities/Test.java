package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.Question.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomTest;
    @OneToMany(mappedBy = "test",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Question> questions;
}
