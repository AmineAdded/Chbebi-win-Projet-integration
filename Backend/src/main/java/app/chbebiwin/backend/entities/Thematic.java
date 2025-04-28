package app.chbebiwin.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Thematic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String image;
    @OneToMany(mappedBy = "thematic",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Chapitre> chapitres;
}
