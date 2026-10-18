package co.edu.unicauca.microservices.pkgUserService;

import java.util.List;

import org.springframework.web.bind.annotation.*;



@RestController 
@RequestMapping ("/api/tbl_users")

public class clsControllerUserSvr {

    private final IUserRepository attIUserRepository;
    private final IRoleRepository attIRoleRepository;

    public clsControllerUserSvr(IUserRepository prmIUserRepository, IRoleRepository prmIRoleRepository) {
        attIUserRepository = prmIUserRepository;
        attIRoleRepository = prmIRoleRepository; 
    }
    //Podemos hacer comentarios inecesarios
    @GetMapping 
    public List<clsUser> opListAll() {
        return attIUserRepository.findAll();
    }

    @PostMapping
    public clsUser opCreate(@RequestBody clsUser prmUser){
        return attIUserRepository.save(prmUser);
    }

    @GetMapping("/{user_ouid}")
    public clsUser opGetForOUID(@PathVariable("user_ouid") String prmOUID){
        return attIUserRepository.findById(prmOUID).orElse(null);
    }

    @PutMapping("/{user_ouid}")
    public clsUser opUpdate(@PathVariable("user_ouid") String prmOUID, @RequestBody clsUser prmUser){
        prmUser.setOUID(prmOUID);
        return attIUserRepository.save(prmUser);
    }

    @DeleteMapping("/{user_ouid}")
    public void opDelete(@PathVariable("user_ouid") String prmOUID) {
        attIUserRepository.deleteById(prmOUID);
    }

    @GetMapping("/name/{user_name}")
    public clsUser opGetForName(@PathVariable("user_name") String prmName) {
        return attIUserRepository.findByAttName(prmName).orElse(null);
    }

    @GetMapping("/roles")
    public List<clsRole> opListAllRoles() {
        return attIRoleRepository.findAll();
    }

    @PostMapping("/roles")
    public clsRole opCreateRole(@RequestBody clsRole prmRole) {
        return attIRoleRepository.save(prmRole);
    }
    
}