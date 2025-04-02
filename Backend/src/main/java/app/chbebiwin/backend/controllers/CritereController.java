package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.CritereService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CritereController {
    private final CritereService critereService;

    public CritereController(CritereService critereService) {
        this.critereService = critereService;
    }
}
