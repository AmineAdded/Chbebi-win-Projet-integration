package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.PersonnaliteService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonnaliteController {
    private final PersonnaliteService personnaliteService;

    public PersonnaliteController(PersonnaliteService personnaliteService){
        this.personnaliteService = personnaliteService;
    }
}
