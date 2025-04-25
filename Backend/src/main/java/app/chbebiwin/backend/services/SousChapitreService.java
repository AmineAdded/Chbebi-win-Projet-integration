package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.SousChapitres;
import app.chbebiwin.backend.repositories.ChapitreRepository;
import app.chbebiwin.backend.repositories.SousChapitreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SousChapitreService {
    @Autowired
    private SousChapitreRepository sousChapitreRepository;
    @Autowired
    private ChapitreRepository chapitreRepository;

    public List<SousChapitres> getChapitresById(Long id){
        return sousChapitreRepository.findAllByChapitreId(id);
    }

    public SousChapitres addSousChapitre(SousChapitres sousChapitre){
        return sousChapitreRepository.save(sousChapitre);
    }
    public SousChapitres upadteSousChapitre(Long id,SousChapitres sousChapitre){
        SousChapitres s = sousChapitreRepository.findById(id).orElseThrow(() ->  new RuntimeException("لم يتم العثور على معرف الفصل الفرعي"));

        s.setDescription(sousChapitre.getDescription());
        s.setImage(sousChapitre.getImage());
        s.setPdf(sousChapitre.getPdf());
        s.setTitle(sousChapitre.getTitle());
        s.setLienVideo(sousChapitre.getLienVideo());
        s.setChapitre(chapitreRepository.findById(sousChapitre.getChapitre().getId()).get());
        s.setLastPageRead(sousChapitre.getLastPageRead());
        s.setCompleted(sousChapitre.isCompleted());

        return sousChapitreRepository.save(s);

    }

    public void deleteSousChapitre(Long id){
        sousChapitreRepository.deleteById(id);
    }
    public List<SousChapitres> getSousChapitres(){
        return sousChapitreRepository.findAll();
    }

    public SousChapitres setlastReadPage(Long id,Long pageNumber) {
        SousChapitres s = sousChapitreRepository.findById(id).get();
        s.setLastPageRead(pageNumber);
        return sousChapitreRepository.save(s);
    }

    public Long getLastReadPage(Long id) {
        return sousChapitreRepository.findById(id).get().getLastPageRead();
    }
}
