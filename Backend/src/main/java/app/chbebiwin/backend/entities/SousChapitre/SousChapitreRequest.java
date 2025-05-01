package app.chbebiwin.backend.entities.SousChapitre;

import lombok.Data;

@Data
public class SousChapitreRequest {
    private String title;
    private String description;
    private String image;
    private String lienVideo;
    private String pdf;
    private Long chapitreId;

}
