package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Personnalite;
import app.chbebiwin.backend.repositories.PersonnaliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnaliteService {
    private final PersonnaliteRepository personnaliteRepository;

    public PersonnaliteService(PersonnaliteRepository personnaliteRepository) {
        this.personnaliteRepository = personnaliteRepository;
    }

    public Personnalite createPersonnalite(Personnalite personnalite) {
        return personnaliteRepository.save(personnalite);
    }
    public String deletePersonnalite(Long id){
        if(personnaliteRepository.existsById(id)){
            personnaliteRepository.deleteById(id);
            return "Personnalité supprimée avec succès!";
        }
        return "Personnalité non trouvée";
    }
    public Personnalite getPersonnaliteById(Long id){
        if(personnaliteRepository.existsById(id)){
            return personnaliteRepository.findById(id).get();
        }
        return null;
    }
    public List<Personnalite> getAllPersonnalites(){
        return personnaliteRepository.findAll();
    }
    public Personnalite updatePersonnalite(Long id, Personnalite personnalite) {
        return personnaliteRepository.findById(id).map(
                existingPersonnalite -> {existingPersonnalite.setNom(personnalite.getNom());
                existingPersonnalite.setContenu(personnalite.getContenu());
                return personnaliteRepository.save(existingPersonnalite);
                })
                .orElseGet(() -> personnaliteRepository.save(personnalite));
    }

}
