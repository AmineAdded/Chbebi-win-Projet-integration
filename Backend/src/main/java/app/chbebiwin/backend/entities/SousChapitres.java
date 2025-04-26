package app.chbebiwin.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SousChapitres {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name = "chapitreId")
    @JsonIgnore
    private Chapitre chapitre;

    private String lienVideo;
    private String pdf;
    private Long lastPageRead;
    private String image;
    private String title;
    private String description;
    @Column(nullable = false)
    private long pourcentage = 0;

}
