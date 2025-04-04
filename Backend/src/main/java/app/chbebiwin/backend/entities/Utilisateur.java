package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.Feedback.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    @Column(nullable = false)
    private String  mdpsCompte;
    private String mdpsSuperAdmin;
    private int role;
    private String typePersonnalite;

    @ManyToOne
    @JoinColumn(name = "Personnalite_id")
    @JsonIgnore
    private Personnalite personnalite;
    @ManyToOne
    @JoinColumn(name = "quote_id")
    @JsonIgnore
    private Quote quote;

    @OneToMany(mappedBy = "utilisateur")
    private List<Feedback> feedbacks;

}
