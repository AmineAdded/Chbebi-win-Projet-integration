package app.chbebiwin.backend.entities.Chapitre;

import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class ChapitreRequest {
    private String title;
    private String description;
    private String image;
    private Long thematicId;
    @Column(nullable = false)
    private long pourcentage = 0;
    @OneToMany(mappedBy = "chapitre")
    @Nullable
    private List<SousChapitres> sousChapitres;
}
