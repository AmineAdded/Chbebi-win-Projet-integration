package app.chbebiwin.backend.entities.SousChapitre;

import app.chbebiwin.backend.entities.Chapitre.Chapitre;
import app.chbebiwin.backend.entities.UserSousChapitreProgress.UserSousChapitreProgress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SousChapitres {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chapitreId")
    @JsonIgnore
    private Chapitre chapitre;

    private String lienVideo;
    private String pdf;
    private String image;
    private String title;
    private String description;

    @OneToMany(mappedBy = "sousChapitre", cascade = CascadeType.ALL)
    private List<UserSousChapitreProgress> userProgresses;

}
