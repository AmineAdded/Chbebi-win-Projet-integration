package app.chbebiwin.backend.entities.Critere;

import lombok.Data;

@Data
public class CritereRequest {
    private String nom;
    private String contenu;
    private Long personnaliteId;
}

