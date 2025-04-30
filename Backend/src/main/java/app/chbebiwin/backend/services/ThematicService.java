package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Thematic;
import app.chbebiwin.backend.repositories.ThematicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThematicService {
    private final ThematicRepository thematicRepository;

    public ThematicService(ThematicRepository thematicRepository) {
        this.thematicRepository = thematicRepository;
    }

    public List<Thematic> getAllThematics() {
        return thematicRepository.findAll();
    }

    public Optional<Thematic> getThematicById(Long id) {
        return thematicRepository.findById(id);
    }

    public Thematic createThematic(Thematic thematic) {
        return thematicRepository.save(thematic);
    }

    public Thematic updateThematic(Long id, Thematic thematicDetails) {
        return thematicRepository.findById(id)
                .map(thematic -> {
                    thematic.setNom(thematicDetails.getNom());
                    thematic.setImage(thematicDetails.getImage());
                    return thematicRepository.save(thematic);
                })
                .orElseGet(() -> thematicRepository.save(thematicDetails));
    }

    public void deleteThematic(Long id) {

        thematicRepository.deleteById(id);
    }
}