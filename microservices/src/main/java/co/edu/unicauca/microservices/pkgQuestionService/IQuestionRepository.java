package co.edu.unicauca.microservices.pkgQuestionService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository 
public interface IQuestionRepository extends JpaRepository<clsQuestion, String>{
    @Query("SELECT q FROM clsQuestion q WHERE q.attQuestionUser = :questionUser")
    List<clsQuestion> findByAttQuestionUser(String prmQuestionUser);
    @Query("SELECT q FROM clsQuestion q WHERE LOWER(q.attQuestionUser) LIKE LOWER(CONCAT('%', :questionUser, '%'))")
    List<clsQuestion> findByAttQuestionUserContainingIgnoreCase(String prmQuestionUser);
}
