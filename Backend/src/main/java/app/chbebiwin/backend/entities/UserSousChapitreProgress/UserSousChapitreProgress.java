package app.chbebiwin.backend.entities.UserSousChapitreProgress;

import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.entities.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSousChapitreProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    @JsonIgnore
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "sous_chapitre_id")
    @JsonIgnore
    private SousChapitres sousChapitre;

    private long lastPageRead = 1;

    private long pourcentage = 0;
}