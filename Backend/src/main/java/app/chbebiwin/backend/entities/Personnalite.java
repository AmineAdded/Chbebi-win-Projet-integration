package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.Critere.Critere;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Personnalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String contenu;
    @OneToMany(mappedBy = "personnalite",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Utilisateur> utilisateurs;

    @OneToMany(mappedBy = "personnalite",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Critere> criteres;
}
