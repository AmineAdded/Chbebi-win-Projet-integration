package app.chbebiwin.backend.services;

import app.chbebiwin.backend.repositories.QuoteRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }
}
