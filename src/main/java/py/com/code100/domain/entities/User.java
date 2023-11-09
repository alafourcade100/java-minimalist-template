package py.com.code100.domain.entities;

import lombok.Getter;
import lombok.Setter;
import py.com.code100.infrastructure.domain.base.BaseEntity;

@Getter
@Setter
public final class User extends BaseEntity {
    private String nombre;
    private String apellido;
    private String email;
    private int edad;

    public User() {
    }

    public User(String nombre, String apellido, String email, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
    }
}
