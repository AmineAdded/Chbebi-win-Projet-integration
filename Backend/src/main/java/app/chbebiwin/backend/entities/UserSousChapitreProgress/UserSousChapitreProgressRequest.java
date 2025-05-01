package app.chbebiwin.backend.entities.UserSousChapitreProgress;

import lombok.Data;

@Data
public class UserSousChapitreProgressRequest {
    private long pourcentage;
    private Long userId;
    private long sousChapitreId;
    private long lastPageRead;
}
