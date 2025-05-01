package app.chbebiwin.backend.entities.UserSousChapitreProgress;

import lombok.Data;

@Data
public class UserSousChapitreProgressRequest {
    private Long pourcentage;
    private Long userId;
    private Long sousChapitreId;
    private Long lastPageRead;
}
