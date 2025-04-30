package app.chbebiwin.backend.services;

import app.chbebiwin.backend.entities.Test;
import app.chbebiwin.backend.repositories.QuoteRepository;
import app.chbebiwin.backend.repositories.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final QuoteRepository quoteRepository;

    public TestService(TestRepository testRepository, QuoteRepository quoteRepository) {
        this.testRepository = testRepository;
        this.quoteRepository = quoteRepository;
    }

    public Test createTest(Test test){
        return testRepository.save(test);
    }
    public String deleteTest(Long id){
        if(testRepository.existsById(id)){
            testRepository.deleteById(id);
            return "Test supprimé avec succès!";
        }
        return "Test non trouvé!";
    }
    public List<Test> getAllTests(){
        return testRepository.findAll();
    }
    public Test getTestById(Long id){
        if(testRepository.existsById(id)){
            return testRepository.findById(id).get();
        }
        return null;
    }
    public Test updateTest(Long id, Test test){
        return testRepository.findById(id)
                .map(existingTest -> {
                    existingTest.setNomTest(test.getNomTest());
                    existingTest.setTypeTest(test.getTypeTest());
                    existingTest.setUtilisable(test.getUtilisable());
                    return testRepository.save(existingTest);
                })
                .orElseGet(() -> testRepository.save(test));
    }
    public String deleteAllTest(){
        testRepository.deleteAll();
        return "Tous les tests sont supprimés!";
    }
    public Test getTestUtilisable() {
        List<Test> tests = testRepository.findAll();
        for (Test test : tests) {
            if ("personnalité".equals(test.getTypeTest()) && test.getUtilisable() == 1) {
                return test;
            }
        }
        throw new RuntimeException("لا يوجد أي اختبار متاح");
    }


}
