package app.chbebiwin.backend.entities.Tache;

import app.chbebiwin.backend.entities.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomTache;
    private int done;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name="userId")
    @JsonIgnore
    private Utilisateur user;
}
