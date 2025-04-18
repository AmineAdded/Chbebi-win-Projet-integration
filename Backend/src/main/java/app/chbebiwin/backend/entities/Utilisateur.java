package app.chbebiwin.backend.entities;

import app.chbebiwin.backend.entities.Feedback.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

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
    private String typePersonnalite;
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

}
