package co.edu.unicauca.microservices.pkgUserService;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<clsRole, String> {
    Optional<clsRole> findByAttName(String prmName);
}