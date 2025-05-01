package app.chbebiwin.backend.entities.Authentification;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProfileRequest {
    private String nom;
    private String email;
    private String currentPassword;
    private String newPassword;
    private Integer role;

}
