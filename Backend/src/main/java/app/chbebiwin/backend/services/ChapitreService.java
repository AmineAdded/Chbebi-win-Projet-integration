package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Chapitre.Chapitre;
import app.chbebiwin.backend.entities.Chapitre.ChapitreRequest;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.entities.Thematic;
import app.chbebiwin.backend.repositories.ChapitreRepository;
import app.chbebiwin.backend.repositories.SousChapitreRepository;
import app.chbebiwin.backend.repositories.ThematicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ChapitreService {
    private static final Logger logger = LoggerFactory.getLogger(ChapitreService.class);

    private final ChapitreRepository chapitreRepository;
    private final ThematicRepository thematicRepository;
    private final SousChapitreRepository sousChapitreRepository;

    @Autowired
    public ChapitreService(
            ChapitreRepository chapitreRepository,
            ThematicRepository thematicRepository,
            SousChapitreRepository sousChapitreRepository) {
        this.chapitreRepository = chapitreRepository;
        this.thematicRepository = thematicRepository;
        this.sousChapitreRepository = sousChapitreRepository;
    }

    public List<Chapitre> getChapitres(Long thematicId) {
        Thematic t = thematicRepository.findById(thematicId)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على هذه الموضوعية"));
        return chapitreRepository.findAllByThematicId(t.getId())
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على أي فصل لهذه الموضوعية"));
    }

    public Chapitre createChapitre(ChapitreRequest request) {
        Thematic thematic = thematicRepository.findById(request.getThematicId())
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على الموضوعية بالمعرف: " + request.getThematicId()));

        Chapitre chapitre = new Chapitre();
        chapitre.setTitle(request.getTitle());
        chapitre.setDescription(request.getDescription());
        chapitre.setImage(request.getImage());
        chapitre.setThematic(thematic);
        chapitre.setPourcentage(request.getPourcentage());

        return chapitreRepository.save(chapitre);
    }

    public Chapitre updateChapitre(Long id, ChapitreRequest request) {
        return chapitreRepository.findById(id)
                .map(existingChapitre -> {
                    existingChapitre.setTitle(request.getTitle());
                    existingChapitre.setDescription(request.getDescription());
                    existingChapitre.setImage(request.getImage());

                    Thematic thematic = thematicRepository.findById(request.getThematicId())
                            .orElseThrow(() -> new RuntimeException("لم يتم العثور على الموضوعية بالمعرف: " + request.getThematicId()));
                    existingChapitre.setThematic(thematic);

                    // The percentage is normally calculated via getProgress, but we can update it if needed
                    if (request.getPourcentage() > 0) {
                        existingChapitre.setPourcentage(request.getPourcentage());
                    }

                    return chapitreRepository.save(existingChapitre);
                })
                .orElseGet(() -> {
                    // If chapitre doesn't exist, create a new one
                    Thematic thematic = thematicRepository.findById(request.getThematicId())
                            .orElseThrow(() -> new RuntimeException("لم يتم العثور على الموضوعية بالمعرف: " + request.getThematicId()));

                    Chapitre newChapitre = new Chapitre();
                    newChapitre.setTitle(request.getTitle());
                    newChapitre.setDescription(request.getDescription());
                    newChapitre.setImage(request.getImage());
                    newChapitre.setThematic(thematic);
                    newChapitre.setPourcentage(request.getPourcentage());

                    return chapitreRepository.save(newChapitre);
                });
    }

    @Transactional
    public String deleteChapitre(Long id) {
        if (chapitreRepository.existsById(id)) {
            Chapitre chapitre = chapitreRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("لم يتم العثور على الفصل بالمعرف: " + id));

            List<SousChapitres> sousChapitres = chapitre.getSousChapitres();
            if (sousChapitres != null && !sousChapitres.isEmpty()) {
                for (SousChapitres sousChapter : sousChapitres) {
                    sousChapitreRepository.deleteById(sousChapter.getId());
                }
                sousChapitres.clear();
            }

            chapitreRepository.deleteById(id);
            return "تم حذف الفصل بنجاح";
        }
        return "الفصل غير موجود";
    }

    public Chapitre getChapitreById(Long id) {
        return chapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على الفصل بالمعرف: " + id));
    }

    public long getProgress(Long id) {
        Chapitre c = chapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على الفصل بالمعرف: " + id));

        List<SousChapitres> s = c.getSousChapitres();
        long sum = 0;

        if (s == null || s.isEmpty()) {
            c.setPourcentage(0);
            chapitreRepository.save(c);
            return 0;
        }

        for (SousChapitres x : s) {
            sum += x.getPourcentage();
        }

        long result = sum / s.size();
        c.setPourcentage(result);
        chapitreRepository.save(c);
        return result;
    }

    @Transactional
    public String deleteAllChapitres() {

        sousChapitreRepository.deleteAll();
        chapitreRepository.deleteAll();
        return "تم حذف جميع الفصول بنجاح";
    }
}