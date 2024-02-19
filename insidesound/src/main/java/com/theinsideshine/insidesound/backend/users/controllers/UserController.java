package com.theinsideshine.insidesound.backend.users.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.models.request.UserRequest;
import com.theinsideshine.insidesound.backend.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

/*
Ventajas de los parámetros de ruta:
Legibilidad de la URL: Los parámetros de ruta suelen hacer que la URL sea más legible y descriptiva.
 Por ejemplo, /users/123 claramente indica que estás accediendo al usuario con el ID 123.
SEO (Optimización para motores de búsqueda): Algunos argumentan que las URLs con parámetros de ruta
son mejores para SEO porque proporcionan información clara sobre la estructura del sitio web
 y el contenido que se está accediendo.
Cacheabilidad: Las solicitudes con parámetros de ruta a menudo son más fáciles de cachear
 en comparación con las solicitudes con parámetros de consulta, ya que los parámetros de ruta
  están directamente en la URL y pueden ser utilizados por los servidores proxy y los navegadores para cachear los recursos.

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        // Lógica para obtener el usuario con el ID especificado
    }
}
postman localhost:8080/users/123

Ventajas de los parámetros de consulta:
Flexibilidad: Los parámetros de consulta son más flexibles y permiten una mayor variedad de
 filtros y opciones. Puedes tener múltiples parámetros de consulta en una sola URL
  y cambiar su orden sin afectar la funcionalidad.
Facilidad de uso en ciertos casos: En algunos casos, puede ser más fácil construir
 y manipular URLs con parámetros de consulta, especialmente cuando los parámetros son opcionales o variables.
Compatibilidad con navegadores y servidores web: Los parámetros de consulta son ampliamente
compatibles con todos los navegadores y servidores web, lo que los hace una opción segura y confiable.
En resumen, la elección entre parámetros de ruta y parámetros de consulta dependerá de factores
como la legibilidad de la URL, los requisitos de SEO, la cacheabilidad de la solicitud
 y la flexibilidad necesaria en tu aplicación. Es importante evaluar cada caso individualmente
  y elegir la opción que mejor se adapte a tus necesidades específicas.


 @RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<?> getUserById(@RequestParam Long id) {
        // Lógica para obtener el usuario con el ID especificado
    }
}
postman localhost:8080/users?id=123


 */







@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService service;

    @GetMapping
    public List<UserRequestDto> list() {

        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<UserRequestDto> list(@PathVariable Integer page) {

        return service.findAll( PageRequest.of(page, 5));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<UserRequestDto> userOptionl = service.findById(id);

        if (userOptionl.isPresent()) {
            return ResponseEntity.ok(userOptionl.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usernames")
    public List<String> getAllUsernames() {
        return service.getAllUsernames();
    }

    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            return validation(result);
        }
        Optional<UserRequestDto> o = service.update(user, id);
        
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<UserRequestDto> o = service.findById(id);


        if (o.isPresent()) {

            try {
                service.remove(id, o.get().getUsername()); //
                return ResponseEntity.noContent().build(); // 204
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al borrar: "+e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
