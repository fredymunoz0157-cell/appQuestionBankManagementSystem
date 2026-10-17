package co.edu.unicauca.microservices.pkgUserService;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name="tbl_role")

@Getter
@Setter 

public class clsRole {
    
    @Id
    @Column(name = "role_ouid")
    private String attOUID;

    @Column (name = "role_name")
    private String attName;

    public clsRole (){}

    public clsRole (String prmOUID, String prmName){
        attOUID=prmOUID;
        attName=prmName;
    }
}
