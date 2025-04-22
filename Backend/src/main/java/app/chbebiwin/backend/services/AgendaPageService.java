package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.AgendaPage;
import app.chbebiwin.backend.repositories.AgendaPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaPageService {

    @Autowired
    private AgendaPageRepository repo;

    public List<AgendaPage> getAllPages(String userId) {
        return repo.findByUserIdOrderByPageNumber(userId);
    }

    public AgendaPage savePage(AgendaPage page) {
        return repo.save(page);
    }

    public void deletePage(Long id) {
        repo.deleteById(id);
    }
}