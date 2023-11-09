package py.com.code100.applications.web.shared;

import lombok.Data;

import java.util.UUID;

@Data
public class UserAddRequest {

    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private int edad;
}
