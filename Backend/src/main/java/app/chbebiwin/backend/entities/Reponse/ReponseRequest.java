package app.chbebiwin.backend.entities.Reponse;

import lombok.Data;

@Data
public class ReponseRequest {
    private String contenu;
    private Long questionId;
}
