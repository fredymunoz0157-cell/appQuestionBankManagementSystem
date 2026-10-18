package co.edu.unicauca.microservices.pkgQuestionService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface IQuestionRepository extends JpaRepository<clsQuestion, String>{
    Optional<clsQuestion> findByAttName(String prmName);
}
