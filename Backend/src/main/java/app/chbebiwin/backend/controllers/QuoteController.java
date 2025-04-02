package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.QuoteService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

}
