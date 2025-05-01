package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.repositories.ChapitreRepository;
import app.chbebiwin.backend.repositories.QuoteRepository;
import app.chbebiwin.backend.repositories.TestRepository;
import app.chbebiwin.backend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private ChapitreRepository chapitreRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long userCount = userRepository.count();
        long chapterCount = chapitreRepository.count();
        long testCount = testRepository.count();
        long quoteCount = quoteRepository.count();

        stats.put("users", userCount);
        stats.put("chapters", chapterCount);
        stats.put("tests", testCount);
        stats.put("quotes", quoteCount);

        return ResponseEntity.ok(stats);
    }
}
