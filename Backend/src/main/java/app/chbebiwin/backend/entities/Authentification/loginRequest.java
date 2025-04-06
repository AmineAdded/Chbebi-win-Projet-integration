package app.chbebiwin.backend.entities.Authentification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class loginRequest {
    private String email;
    private String password;
}
