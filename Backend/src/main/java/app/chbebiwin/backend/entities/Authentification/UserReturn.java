package app.chbebiwin.backend.entities.Authentification;

import lombok.Data;

@Data
public class UserReturn {
    private String nom;
    private String email;
    private String mdpsCompte;
    private int role;
    private String accessToken;
    private Long personnalite_id;
}
