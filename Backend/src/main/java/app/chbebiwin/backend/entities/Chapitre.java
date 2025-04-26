package app.chbebiwin.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import app.chbebiwin.backend.entities.SousChapitres;

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

    @OneToMany(mappedBy = "chapitre")
    private List<SousChapitres> sousChapitres;
    @Column(nullable = false)
    private long pourcentage = 0;


}
