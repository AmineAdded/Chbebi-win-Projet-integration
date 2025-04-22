package app.chbebiwin.backend.entities;

import jakarta.persistence.*;

@Entity
public class AgendaPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pageNumber;

    @Lob
    private String leftContent;

    @Lob
    private String rightContent;

    private String userId;

    // Getters & Setters
}