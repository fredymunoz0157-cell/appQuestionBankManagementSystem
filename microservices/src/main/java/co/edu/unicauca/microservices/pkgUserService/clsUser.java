package co.edu.unicauca.microservices.pkgUserService;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name="tbl_user")

@Getter
@Setter 

public class clsUser {
    
    @Id
    @Column(name = "user_ouid")
    private String attOUID;

    @Column (name = "user_name")
    private String attName;

    @Column (name = "user_password")
    private String attPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "tbl_users_roles", 
        joinColumns = @JoinColumn(name = "user_ouid"), 
        inverseJoinColumns = @JoinColumn(name = "role_ouid")
    )
    private Set<clsRole> attRoles= new HashSet<>();

    public clsUser () {}

    public clsUser (String prmOUID, String prmName, String prmPassword, Set<clsRole> prmRoles){
        attOUID=prmOUID;
        attName=prmName;
        attPassword=prmPassword;
        attRoles=prmRoles;
    }
}
