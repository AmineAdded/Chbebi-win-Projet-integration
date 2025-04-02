package app.chbebiwin.backend.services;

import app.chbebiwin.backend.repositories.CritereRepository;
import org.springframework.stereotype.Service;

@Service
public class CritereService {
    private final CritereRepository critereRepository;

    public CritereService(CritereRepository critereRepository) {
        this.critereRepository = critereRepository;
    }
}
