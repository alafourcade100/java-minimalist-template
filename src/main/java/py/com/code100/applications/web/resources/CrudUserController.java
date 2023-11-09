package py.com.code100.applications.web.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.com.code100.domain.entities.User;
import py.com.code100.domain.services.user.CrudUserService;
import py.com.code100.infrastructure.persistence.base.FilterPaginationQueryModel;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CrudUserController {

    private final CrudUserService service;


    @GetMapping("")
    public ResponseEntity<Object> getAll(@RequestParam(value = "filters", required = false) List<String> filters,
                                       @RequestParam(value = "orders", required = false) List<String> orders,
                                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "0") int pageSize) {

        var filterPagination = new FilterPaginationQueryModel(filters, orders, page, pageSize);
        var response = service.listar(filterPagination);
        return new ResponseEntity<>(response.getValue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
        var response = service.obtenerPorId(id);
        if (!response.isSuccess()){
            return new ResponseEntity<>(response.getErrorResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getValue(), HttpStatus.OK);
    }

    @PostMapping("")
    public  ResponseEntity<Object> crear(@RequestBody User user) {
        var response = service.add(user);
        if (!response.isSuccess()){
            return new ResponseEntity<>(response.getErrorResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getValue(), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Object> actualizar(@RequestBody User user) {

        var response = service.actualizar(user);
        if (!response.isSuccess()){
            return new ResponseEntity<>(response.getErrorResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getValue(), HttpStatus.OK);
    }

}
