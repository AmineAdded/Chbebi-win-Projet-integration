package app.chbebiwin.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chapitre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String description;
    private String lienVideo;
    private String image;
    private String pdf;
    @ManyToOne
    @JoinColumn(name = "thematic_id")
    private Thematic thematic;
}
