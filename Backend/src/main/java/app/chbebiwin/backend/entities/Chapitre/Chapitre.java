package app.chbebiwin.backend.entities.Chapitre;

import app.chbebiwin.backend.entities.Thematic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chapitre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name = "thematic_id")
    @JsonIgnore
    private Thematic thematic;

    @OneToMany(mappedBy = "chapitre",cascade = CascadeType.ALL,orphanRemoval = true)
    @Nullable
    private List<SousChapitres> sousChapitres;
    @Column(nullable = false)
    private long pourcentage = 0;


}
