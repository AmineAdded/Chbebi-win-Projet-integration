package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.SousChapitre.SousChapitreRequest;
import app.chbebiwin.backend.entities.SousChapitre.SousChapitres;
import app.chbebiwin.backend.services.SousChapitreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public SousChapitres createSousChapitres(@RequestBody SousChapitreRequest sousChapitreRequest){
        return sousChapitreService.addSousChapitre(sousChapitreRequest);
    }

    @PutMapping("/updateSousChapitre/{id}")
    public SousChapitres upadteSousChapitre(@PathVariable Long id, @RequestBody SousChapitreRequest sousChapitreRequest){
        return sousChapitreService.updateSousChapitre(id, sousChapitreRequest);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteSousChapitre(@PathVariable Long id){
        sousChapitreService.deleteSousChapitre(id);
    }
//    @PutMapping("/setLastPageRead/{id}")
//    public SousChapitres setLastReadPage(@PathVariable Long id, @RequestBody SousChapitreRequest sousChapitresRequest) {
//        //Long pageNumber = requestBody.get("pageNumber");
//        return sousChapitreService.setLastReadPage(id, sousChapitresRequest);
//    }
//    @GetMapping("/getLastReadPage/{id}")
//    public SousChapitres getLastReadPage(@PathVariable Long id){
//        return sousChapitreService.getLastReadPage(id);
//    }


}
