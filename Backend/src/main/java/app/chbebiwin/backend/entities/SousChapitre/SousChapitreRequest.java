package app.chbebiwin.backend.entities.SousChapitre;


import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SousChapitreRequest {
    private String title;
    private String description;
    private String image;
    private String lienVideo;
    private String pdf;
    @Nullable
    private Long lastPageRead;
    @Column(nullable = false)
    private long pourcentage = 0;
    @Nullable
    private Long userId;

    private Long chapitreId;

}
