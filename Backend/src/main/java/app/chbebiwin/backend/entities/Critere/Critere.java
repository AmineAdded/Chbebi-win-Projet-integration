package app.chbebiwin.backend.entities.Critere;

import app.chbebiwin.backend.entities.Personnalite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Critere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String contenu;

    @ManyToOne
    @JoinColumn(name = "Personnalite_id")
    @JsonIgnore
    private Personnalite personnalite;
}
