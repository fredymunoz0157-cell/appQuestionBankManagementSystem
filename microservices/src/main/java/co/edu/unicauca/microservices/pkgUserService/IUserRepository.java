package co.edu.unicauca.microservices.pkgUserService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<clsUser, String> {
    @Query("SELECT u FROM clsUser u WHERE LOWER(u.attName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<clsUser> opGetUserWithName(@Param("name") String name);
}
