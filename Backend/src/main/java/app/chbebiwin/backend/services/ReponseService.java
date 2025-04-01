package app.chbebiwin.backend.services;

import app.chbebiwin.backend.repositories.ReponseRepository;
import org.springframework.stereotype.Service;

@Service
public class ReponseService {
    private final ReponseRepository reponseRepository;

    public ReponseService(ReponseRepository reponseRepository) {
        this.reponseRepository = reponseRepository;
    }
}
