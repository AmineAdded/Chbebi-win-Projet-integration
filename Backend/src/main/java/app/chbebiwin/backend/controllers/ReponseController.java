package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.ReponseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReponseController {
    private final ReponseService reponseService;

    public ReponseController(ReponseService reponseService) {
        this.reponseService = reponseService;
    }
}
