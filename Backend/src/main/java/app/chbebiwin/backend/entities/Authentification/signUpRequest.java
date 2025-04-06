package app.chbebiwin.backend.entities.Authentification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class signUpRequest {
    private String nom;
    private String email;
    private String password;
    private String confirmPassword;
}
