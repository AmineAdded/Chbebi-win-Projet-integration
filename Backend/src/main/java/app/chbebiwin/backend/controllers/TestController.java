package app.chbebiwin.backend.controllers;

import app.chbebiwin.backend.entities.Test;
import app.chbebiwin.backend.services.TestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/create")
    public Test create(@RequestBody Test test) {
        return testService.createTest(test);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(Long id) {
        return testService.deleteTest(id);
    }
    @GetMapping("/get/{id}")
    public Test getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }
    @GetMapping("/getAll")
    public List<Test> getAll() {
        return testService.getAllTests();
    }
    @PutMapping("/update/{id}")
    public Test update(@RequestBody Test test, @PathVariable Long id) {
        return testService.updateTest(id, test);
    }
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        return testService.deleteAllTest();
    }
}
