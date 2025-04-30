package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.Question.Question;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomTest;
    private String typeTest;
    private int utilisable;
    @OneToMany(mappedBy = "test",cascade = CascadeType.ALL,orphanRemoval = true)
    @Nullable
    private List<Question> questions;
}
