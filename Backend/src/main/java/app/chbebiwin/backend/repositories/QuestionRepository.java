package app.chbebiwin.backend.repositories;

import app.chbebiwin.backend.entities.Question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTestId(Long testId);

}
