package py.com.code100.persistence.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.code100.infrastructure.persistence.base.BaseDbModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserModelJPA extends BaseDbModel {

    @Basic(optional = false)
    private  String nombre;
    @Basic(optional = false)
    private  String apellido;
    @Basic(optional = false)
    private  String email;
    @Basic(optional = false)
    private  int edad;
}
