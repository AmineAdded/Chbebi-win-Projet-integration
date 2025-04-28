package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Chapitre;
import app.chbebiwin.backend.entities.SousChapitres;
import app.chbebiwin.backend.entities.Thematic;
import app.chbebiwin.backend.repositories.ChapitreRepository;
import app.chbebiwin.backend.repositories.ThematicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChapitreService {
    @Autowired
    private ChapitreRepository chapitreRepository;
    @Autowired
    private ThematicRepository thematicRepository;

    public List<Chapitre> getChapitres(Long thematicId){
        Thematic t = thematicRepository.findById(thematicId).get();
        return chapitreRepository.findAllByThematicId(t.getId()).orElseThrow(() -> new RuntimeException("لم يتم العثور على أي فصل لهذه الموضوعية"));
    }
    public Chapitre createChapitre(Chapitre chapitre){
        return chapitreRepository.save(chapitre);
    }
    public Chapitre updateChapitre(Long id,Chapitre chapitre){
        Chapitre c = chapitreRepository.findById(id).get();
        c.setDescription(chapitre.getDescription());
        c.setTitle(chapitre.getTitle());
        c.setImage(chapitre.getImage());
        c.setThematic(chapitre.getThematic());

        return chapitreRepository.save(c);

    }

    public String deleteChapitre(Long id){
        if(chapitreRepository.existsById(id)){
            chapitreRepository.deleteById(id);
            return "تم حذف الفصل بنجاح";
        }
        return "الفصل غير موجود";
    }

    public long getProgress(Long id) {
        Chapitre c = chapitreRepository.findById(id).get();
        List<SousChapitres> s = c.getSousChapitres();
        long sum = 0;

        for (SousChapitres x : s) {
            sum += x.getPourcentage();
        }

        if (s.size() == 0) {
            c.setPourcentage(0);
            return 0;
        }

        long result = sum / s.size();
        c.setPourcentage(result);
        chapitreRepository.save(c);
        return result;
    }


}
