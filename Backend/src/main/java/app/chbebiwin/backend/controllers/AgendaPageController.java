package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.AgendaPage.AgendaPage;
import app.chbebiwin.backend.entities.AgendaPage.AgendaPageRequest;
import app.chbebiwin.backend.entities.Utilisateur;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import app.chbebiwin.backend.services.AgendaPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaPageController {

    @Autowired
    private AgendaPageService service;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/{id}")
    public List<AgendaPage> getAllPages(@PathVariable Long id) {
        return service.getAllPages(id);
    }

    @PostMapping("/save")
    public AgendaPage savePage(@RequestBody AgendaPageRequest dto) {
        AgendaPage page = new AgendaPage();
        page.setId(dto.id);
        page.setPageNumber(dto.pageNumber);
        page.setLeftContent(dto.leftContent);
        page.setRightContent(dto.rightContent);

        Utilisateur utilisateur = utilisateurRepository.findById(dto.utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur not found"));
        page.setUtilisateur(utilisateur);

        return service.savePage(page);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePage(@PathVariable Long id) {
        service.deletePage(id);
    }
}