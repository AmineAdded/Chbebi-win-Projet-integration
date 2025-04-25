package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Tache.Tache;
import app.chbebiwin.backend.entities.Tache.TacheRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.TacheRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TacheService {
    @Autowired
    private TacheRepository tacheRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Tache create(TacheRequest tache){
        Tache t = new Tache();
        Utilisateur u = utilisateurRepository.findById(tache.getUserId()).get();
        if(u == null) throw new RuntimeException("لمستخدم ذو المعرف: " + tache.getUserId() + " غير موجود");
        t.setDate(tache.getDate());
        t.setNomTache(tache.getNomTache());
        t.setDone(tache.getDone());
        t.setUser(u);
        return tacheRepository.save(t);
    }
    public List<Tache> getTachesUser(Long id, LocalDate date){
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
        List<Tache> l = tacheRepository.findAllByUser(u);
        List<Tache> filtered = new ArrayList<>();

        for (Tache t : l) {
            if (t.getDate().equals(date)) {
                filtered.add(t);
            }
        }

        return filtered;
    }
    public Tache putTache(Long id,Tache tache){
        Tache t = tacheRepository.findById(id).get();
        t.setDone(tache.getDone());
        return tacheRepository.save(t);
    }
}
