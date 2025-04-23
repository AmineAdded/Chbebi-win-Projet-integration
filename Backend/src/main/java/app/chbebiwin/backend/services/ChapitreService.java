package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Chapitre;
import app.chbebiwin.backend.repositories.ChapitreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapitreService {
    private final ChapitreRepository chapitreRepository;

    public ChapitreService(ChapitreRepository chapitreRepository) {
        this.chapitreRepository = chapitreRepository;
    }

    public List<Chapitre> getAllChapitres() {
        return chapitreRepository.findAll();
    }

    public Optional<Chapitre> getChapitreById(Long id) {
        return chapitreRepository.findById(id);
    }

    public Chapitre createChapitre(Chapitre chapitre) {
        return chapitreRepository.save(chapitre);
    }

    public Chapitre updateChapitre(Long id, Chapitre chapitreDetails) {
        return chapitreRepository.findById(id).map(chapitre -> {
            chapitre.setTitle(chapitreDetails.getTitle());
            chapitre.setDescription(chapitreDetails.getDescription());
            chapitre.setLienVideo(chapitreDetails.getLienVideo());
            chapitre.setImage(chapitreDetails.getImage());
            chapitre.setPdf(chapitreDetails.getPdf());
            chapitre.setThematic(chapitreDetails.getThematic());
            return chapitreRepository.save(chapitre);
        }).orElseGet(() -> {
            chapitreDetails.setId(id);
            return chapitreRepository.save(chapitreDetails);
        });
    }

    public void deleteChapitre(Long id) {
        chapitreRepository.deleteById(id);
    }

    public List<Chapitre> getAllSuperChapitres() {
        return chapitreRepository.findByIsSuperChapitreTrue();
    }

    public List<Chapitre> getAllSousChapitres() {
        return chapitreRepository.findByIsSuperChapitreFalse();
    }
    public List<Chapitre> getSousChapitresBySuperChapitreId(Long superChapitreId) {
        return chapitreRepository.findBySuperChapitreId(superChapitreId);
    }
    public List<Chapitre> getAllSuperChapitresByThematic(Long thematicId) {
        return chapitreRepository.findByIsSuperChapitreTrueAndThematicId(thematicId);
    }
}