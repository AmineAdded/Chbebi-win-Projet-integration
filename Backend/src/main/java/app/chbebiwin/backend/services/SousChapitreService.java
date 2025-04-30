package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Chapitre.Chapitre;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitreRequest;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.ChapitreRepository;
import app.chbebiwin.backend.repositories.SousChapitreRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SousChapitreService {
    private static final Logger logger = LoggerFactory.getLogger(SousChapitreService.class);

    private final SousChapitreRepository sousChapitreRepository;
    private final ChapitreRepository chapitreRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public SousChapitreService(SousChapitreRepository sousChapitreRepository,
                               ChapitreRepository chapitreRepository,
                               UtilisateurRepository utilisateurRepository) {
        this.sousChapitreRepository = sousChapitreRepository;
        this.chapitreRepository = chapitreRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<SousChapitres> getChapitresById(Long id) {
        return sousChapitreRepository.findAllByChapitreId(id);
    }

    public SousChapitres addSousChapitre(SousChapitreRequest request) {
        Chapitre chapitre = chapitreRepository.findById(request.getChapitreId())
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف الفصل"));

        SousChapitres sousChapitre = new SousChapitres();
        sousChapitre.setTitle(request.getTitle());
        sousChapitre.setDescription(request.getDescription());
        sousChapitre.setImage(request.getImage());
        sousChapitre.setLienVideo(request.getLienVideo());
        sousChapitre.setPdf(request.getPdf());
        sousChapitre.setLastPageRead(request.getLastPageRead());
        sousChapitre.setPourcentage(request.getPourcentage());
        sousChapitre.setChapitre(chapitre);

        if (request.getUserId() != null) {
            Utilisateur user = utilisateurRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف المستخدم"));
            sousChapitre.setUser(user);
        }

        return sousChapitreRepository.save(sousChapitre);
    }

    public SousChapitres updateSousChapitre(Long id, SousChapitreRequest request) {
        SousChapitres sousChapitre = sousChapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف الفصل الفرعي"));

        Chapitre chapitre = chapitreRepository.findById(request.getChapitreId())
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف الفصل"));

        sousChapitre.setTitle(request.getTitle());
        sousChapitre.setDescription(request.getDescription());
        sousChapitre.setImage(request.getImage());
        sousChapitre.setLienVideo(request.getLienVideo());
        sousChapitre.setPdf(request.getPdf());
        sousChapitre.setLastPageRead(request.getLastPageRead());
        sousChapitre.setChapitre(chapitre);

        // Update user if userId is provided
        if (request.getUserId() != null) {
            Utilisateur user = utilisateurRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف المستخدم"));
            sousChapitre.setUser(user);
        }

        return sousChapitreRepository.save(sousChapitre);
    }

    public void deleteSousChapitre(Long id) {
        if (!sousChapitreRepository.existsById(id)) {
            throw new RuntimeException("لم يتم العثور على الفصل الفرعي بالمعرف: " + id);
        }
        sousChapitreRepository.deleteById(id);
    }

    public List<SousChapitres> getSousChapitres() {
        return sousChapitreRepository.findAll();
    }

    public SousChapitres setLastReadPage(Long id, SousChapitreRequest request) {
        SousChapitres sousChapitre = sousChapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف الفصل الفرعي"));

        sousChapitre.setLastPageRead(request.getLastPageRead());
        sousChapitre.setPourcentage(request.getPourcentage());

        return sousChapitreRepository.save(sousChapitre);
    }

    public SousChapitres getLastReadPage(Long id) {
        return sousChapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على معرف الفصل الفرعي"));
    }

    public SousChapitres getSousChapitreById(Long id) {
        return sousChapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على الفصل الفرعي بالمعرف: " + id));
    }
}