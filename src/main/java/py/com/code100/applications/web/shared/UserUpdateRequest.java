package py.com.code100.applications.web.shared;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdateRequest {

    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private int edad;
}
