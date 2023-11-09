package py.com.code100.infrastructure.domain.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    protected UUID id;
    protected boolean isActive;
    protected Date createAt;
    protected Date updateAt;
    protected Date deleteAt;

    public BaseEntity(UUID id) {
        this.id = id;
        this.isActive = true;
        this.createAt = new Date();
        this.updateAt = new Date();
        this.deleteAt = null;
    }
}