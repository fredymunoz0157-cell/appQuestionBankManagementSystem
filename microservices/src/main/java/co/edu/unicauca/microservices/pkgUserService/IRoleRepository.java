package co.edu.unicauca.microservices.pkgUserService;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<clsRole, String> {
    @Query("SELECT r FROM clsRole r WHERE LOWER(r.attName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<clsRole> buscarPorNombre(@Param("name") String name);
}