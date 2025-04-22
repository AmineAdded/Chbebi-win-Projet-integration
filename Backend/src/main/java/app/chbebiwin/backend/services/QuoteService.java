package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Quote;
import app.chbebiwin.backend.repositories.QuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }
    public String deleteQuote(Long id) {
        if (quoteRepository.existsById(id)) {
            quoteRepository.deleteById(id);
            return "Quote supprimé avec succès!";
        } else {
            return "Quote non trouvé!";
        }
    }
    public List<Quote> getAllQuote(){
        return quoteRepository.findAll();
    }

    public Quote getQuoteById(Long id) {
        if(quoteRepository.existsById(id)){
            return quoteRepository.findById(id).get();
        }
        return null;
    }
    public Quote updateQuote(Quote quote, Long id) {
        return quoteRepository.findById(id)
                .map(existingQuote -> {
                    existingQuote.setNomAuteur(quote.getNomAuteur());
                    existingQuote.setContenu(quote.getContenu());
                    return quoteRepository.save(existingQuote);
                })
                .orElseGet(() -> quoteRepository.save(quote));
    }
    public String deleteAllQuote(){
        quoteRepository.deleteAll();
        return "Tous les quotes sont supprimés!";
    }

    public Quote getRandomQuote() {
        List<Quote> allQuotes = quoteRepository.findAll();
        if (allQuotes.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * allQuotes.size());
        return allQuotes.get(randomIndex);
    }

}
