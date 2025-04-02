package app.chbebiwin.backend.services;

import app.chbebiwin.backend.repositories.PersonnaliteRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonnaliteService {
    private final PersonnaliteRepository personnaliteRepository;

    public PersonnaliteService(PersonnaliteRepository personnaliteRepository) {
        this.personnaliteRepository = personnaliteRepository;
    }
}
