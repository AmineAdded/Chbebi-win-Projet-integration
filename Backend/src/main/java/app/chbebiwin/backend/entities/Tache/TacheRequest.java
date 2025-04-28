package app.chbebiwin.backend.entities.Tache;

import app.chbebiwin.backend.entities.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TacheRequest {
    private String nomTache;
    private int done;
    private LocalDate date;
    private Long userId;
}
