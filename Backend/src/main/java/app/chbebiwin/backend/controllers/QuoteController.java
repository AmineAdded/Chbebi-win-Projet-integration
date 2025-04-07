package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Quote;
import app.chbebiwin.backend.services.QuoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Quote")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/create")
    public Quote create(@RequestBody Quote quote) {
        return quoteService.createQuote(quote);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
         return quoteService.deleteQuote(id);
    }
    @GetMapping("/getAll")
    public List<Quote> getAll() {
        return quoteService.getAllQuote();
    }
    @GetMapping("/get/{id}")
    public Quote getQuoteById(@PathVariable Long id) {
        return quoteService.getQuoteById(id);
    }
    @PutMapping("/update/{id}")
    public Quote update(@RequestBody Quote quote,@PathVariable Long id){
        return quoteService.updateQuote(quote,id);
    }
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return quoteService.deleteAllQuote();
    }

}
