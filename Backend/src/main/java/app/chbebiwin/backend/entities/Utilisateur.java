package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.AgendaPage.AgendaPage;
import app.chbebiwin.backend.entities.Feedback.Feedback;
import app.chbebiwin.backend.entities.Personnalite.Personnalite;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.entities.Tache.Tache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String  mdpsCompte;
    private String mdpsSuperAdmin;
    private int role =0;
    private String resetToken;
    private LocalDateTime tokenExpiry;
    private String accessToken;


    @ManyToOne
    @JoinColumn(name = "Personnalite_id")
    @JsonIgnore
    private Personnalite personnalite;
    @ManyToOne
    @JoinColumn(name = "quote_id")
    @JsonIgnore
    private Quote quote;

    @OneToMany(mappedBy = "utilisateur",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "utilisateur",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AgendaPage> agendaPage;

    @OneToMany(mappedBy = "user")
    private List<Tache> taches;

    @OneToMany(mappedBy = "user")
    private List<SousChapitres> sousChapitres;


}
