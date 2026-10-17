package co.edu.unicauca.microservices.pkgUserService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface IUserRepository extends JpaRepository<clsUser, String>{
    Optional<clsUser> findByAttName(String prmName);
}
