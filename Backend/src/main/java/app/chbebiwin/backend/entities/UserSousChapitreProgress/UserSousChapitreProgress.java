package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
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
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "sous_chapitre_id")
    private SousChapitres sousChapitre;

    private Long lastPageRead;

    private long pourcentage = 0;
}