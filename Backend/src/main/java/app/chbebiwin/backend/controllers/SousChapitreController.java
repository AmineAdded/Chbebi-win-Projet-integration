package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.SousChapitres;
import app.chbebiwin.backend.services.SousChapitreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/SousChapitres")
@CrossOrigin (origins = "*")
public class SousChapitreController {
    @Autowired
    private SousChapitreService sousChapitreService;

    @GetMapping("/getByChapitreId/{id}")
    public List<SousChapitres> getSousChapitres(@PathVariable Long id){
        return sousChapitreService.getChapitresById(id);
    }
    @PostMapping("/createSousChapitre")
    public SousChapitres createSousChapitres(@RequestBody SousChapitres sousChapitre){
        return sousChapitreService.addSousChapitre(sousChapitre);
    }

    @PutMapping("/updateSousChapitre/{id}")
    public SousChapitres upadteSousChapitre(@PathVariable Long id, @RequestBody SousChapitres sousChapitre){
        return sousChapitreService.upadteSousChapitre(id, sousChapitre);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteSousChapitre(@PathVariable Long id){
        sousChapitreService.deleteSousChapitre(id);
    }
    @PutMapping("/setLastPageRead/{id}")
    public SousChapitres setLastReadPage(@PathVariable Long id, @RequestBody SousChapitres sousChapitres) {
        //Long pageNumber = requestBody.get("pageNumber");
        return sousChapitreService.setlastReadPage(id, sousChapitres);
    }
    @GetMapping("/getLastReadPage/{id}")
    public SousChapitres getLastReadPage(@PathVariable Long id){
        return sousChapitreService.getLastReadPage(id);
    }

}
