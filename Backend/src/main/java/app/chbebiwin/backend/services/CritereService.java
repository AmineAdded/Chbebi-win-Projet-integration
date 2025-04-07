package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Critere.Critere;
import app.chbebiwin.backend.entities.Critere.CritereRequest;
import app.chbebiwin.backend.entities.Personnalite;
import app.chbebiwin.backend.repositories.CritereRepository;
import app.chbebiwin.backend.repositories.PersonnaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CritereService {
    private final CritereRepository critereRepository;
    private final PersonnaliteRepository personnaliteRepository;

    @Autowired
    public CritereService(CritereRepository critereRepository,PersonnaliteRepository personnaliteRepository) {
        this.critereRepository = critereRepository;
        this.personnaliteRepository = personnaliteRepository;
    }

    public Critere createCritere(CritereRequest request) {
        Personnalite personnalite = personnaliteRepository.findById(request.getPersonnaliteId())
                .orElseThrow(() -> new RuntimeException("Personnalité introuvable avec ID: "+request.getPersonnaliteId()));
        Critere critere = new Critere();
        critere.setNom(request.getNom());
        critere.setContenu(request.getContenu());
        critere.setPersonnalite(personnalite);

        return critereRepository.save(critere);
    }
    public String deleteCritere(Long id) {
        if(critereRepository.existsById(id)) {
            critereRepository.findById(id).get();
            return "Critére supprimé avec succès!";
        }
        return "Critére non trouvé!";
    }
    public Critere getCritereById(Long id){
        if(critereRepository.existsById(id)){
            return critereRepository.findById(id).get();
        }
        return null;
    }
    public List<Critere> getAllCritere(){
        return critereRepository.findAll();
    }
    public Critere updateCritere(Long id, CritereRequest request) {
        Personnalite personnalite = personnaliteRepository.findById(request.getPersonnaliteId())
                .orElseThrow(() -> new RuntimeException("Personnalité introuvable avec ID: " + request.getPersonnaliteId()));

        return critereRepository.findById(id)
                .map(existingCritere -> {
                    existingCritere.setNom(request.getNom());
                    existingCritere.setContenu(request.getContenu());
                    existingCritere.setPersonnalite(personnalite);
                    return critereRepository.save(existingCritere);
                })
                .orElseGet(() -> {
                    Critere newCritere = new Critere();
                    newCritere.setNom(request.getNom());
                    newCritere.setContenu(request.getContenu());
                    newCritere.setPersonnalite(personnalite);
                    return critereRepository.save(newCritere);
                });
    }
    public String deleteAllCriteres(){
        critereRepository.deleteAll();
        return "Tous les critéres sont supprimés!";
    }


}
