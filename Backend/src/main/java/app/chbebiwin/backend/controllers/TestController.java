package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.services.TestService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }
}
