package app.chbebiwin.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String title;
    private String description;
    private String lienVideo;
    private String image;
    private String pdf;
    @ManyToOne
    @JoinColumn(name = "thematic_id")
    @JsonIgnore
    private Thematic thematic;
    private boolean isSuperChapitre;
    @Column(nullable = true)
    private Long superChapitreId;
}
