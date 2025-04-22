package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.WorkshopEvents;
import app.chbebiwin.backend.repositories.WorkshopEventsRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorkshopEventsService {
    private final WorkshopEventsRepository workshopEventsRepository;

    public WorkshopEventsService(WorkshopEventsRepository workshopEventsRepository){
        this.workshopEventsRepository = workshopEventsRepository;
    }
    public WorkshopEvents createWorkshopEvents(WorkshopEvents workshopEvents) {
        return workshopEventsRepository.save(workshopEvents);
    }
    public String deleteWorkshopEvents(Long id) {
        if(workshopEventsRepository.existsById(id)){
            workshopEventsRepository.deleteById(id);
            return "Workshop events est supprimée!";
        }
        return "Workshop events non trouvée";
    }
    public WorkshopEvents getWorkshopEventsById(Long id){
        if(workshopEventsRepository.existsById(id)){
            return workshopEventsRepository.findById(id).get();
        }
        return null;
    }

    public List<WorkshopEvents> getAllWorkshopEvents() {
        return workshopEventsRepository.findAll();
    }

    public WorkshopEvents updateWorkshopEvents(Long id, WorkshopEvents workshopEvents) {
        return workshopEventsRepository.findById(id).map(
                existingWorkshopEvents -> {existingWorkshopEvents.setNom(workshopEvents.getNom());
                    existingWorkshopEvents.setDescription(workshopEvents.getDescription());
                    existingWorkshopEvents.setDate(workshopEvents.getDate());
                    existingWorkshopEvents.setImage(workshopEvents.getImage());
                    existingWorkshopEvents.setLien(workshopEvents.getLien());
                    return workshopEventsRepository.save(existingWorkshopEvents);
                }).orElseGet(() -> workshopEventsRepository.save(workshopEvents));
    }
    public String deleteAllWorkshopEvents(){
        workshopEventsRepository.deleteAll();
        return "Tous les workshops et events sont supprimés!";
    }

    public String deletePastWorkshopEvents() {
        List<WorkshopEvents> all = workshopEventsRepository.findAll();
        Date now = new Date();

        List<WorkshopEvents> expired = all.stream()
                .filter(e -> e.getDate().before(now))
                .toList();

        workshopEventsRepository.deleteAll(expired);
        return expired.size() + " événements expirés supprimés";
    }

}
