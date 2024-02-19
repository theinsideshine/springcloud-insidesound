package com.theinsideshine.insidesound.backend.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.theinsideshine.insidesound.backend.users.models.IUser;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.mapper.DtoMapperUser;
import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.models.request.UserRequest;
import com.theinsideshine.insidesound.backend.users.repositories.RoleRepository;
import com.theinsideshine.insidesound.backend.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*

En Spring, puedes inyectar dependencias de componentes de varias maneras. La diferencia principal radica en cómo se realiza la inyección de dependencias.

Inyección a través del constructor:

En este enfoque, las dependencias se inyectan a través del constructor de la clase.
Es una forma explícita de inyección de dependencias, ya que todas las dependencias necesarias
 se pasan como argumentos al constructor.
Es fácil de entender y de probar, ya que las dependencias se pueden proporcionar como argumentos
 al instanciar la clase en las pruebas unitarias.
Promueve la inmutabilidad, ya que las dependencias se establecen una vez durante la construcción
 de la clase y no pueden cambiar después de eso, lo que puede mejorar la seguridad y la robustez.

Inyección mediante la anotación @Autowired:

En este enfoque, Spring busca automáticamente un bean que coincida con el tipo requerido y lo inyecta en la clase.
Es menos explícito que la inyección a través del constructor, ya que no se ven las dependencias directamente
 en la firma del constructor.
Puede simplificar la escritura del código, ya que no es necesario escribir un constructor explícito
 para inyectar dependencias.
Sin embargo, puede hacer que las dependencias sean menos evidentes, lo que puede dificultar la comprensión
del flujo de dependencias y la configuración de la clase.
Si hay múltiples beans del mismo tipo en el contexto de Spring, puede haber ambigüedad en la inyección,
lo que puede requerir configuración adicional.
En general, la inyección a través del constructor se considera una práctica recomendada,
ya que hace que las dependencias sean explícitas y facilita la comprensión y la prueba del código.
 Sin embargo, la inyección mediante @Autowired puede ser más conveniente en ciertos casos,
  especialmente en clases con muchas dependencias donde escribir un constructor largo puede volverse engorroso.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public List<UserRequestDto> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(u -> DtoMapperUser.builder().setUser(u).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRequestDto> findAll(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(u -> DtoMapperUser.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRequestDto> findById(Long id) {
        return repository.findById(id).map(u -> DtoMapperUser
                .builder()
                .setUser(u)
                .build());

    }

    public List<String> getAllUsernames() {
        Iterable<User> usersIterable = repository.findAll();
        List<User> usersList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        return usersList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserRequestDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles( user));
        return DtoMapperUser.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserRequestDto> update(UserRequest user, Long id) {
        Optional<User> o = repository.findById(id);
        User userOptional = null;
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setRoles(getRoles(user));
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = repository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(Long id,String username) {
        repository.deleteById(id);

/*
        // Llama al cliente Feign de "tracks" para poner borrar los track con el username
        ResponseEntity<?> trackResponse = trackClientRest.removeTracksByUsername(username);;

        if (trackResponse.getStatusCode().is5xxServerError()) {

            throw new EntityNotFoundException("Error al borrar tracks con el username"); // Hubo error en el servidor
        }

        // Llama al cliente Feign de "album" para poner borrar los track con el username
        ResponseEntity<?> albumResponse = albumClientRest.removeAlbumsByUsername(username);;

        if (albumResponse.getStatusCode().is5xxServerError()) {

            throw new EntityNotFoundException("Error al borrar albumes con el username"); // Hubo error en el servidor
        }*/

    }

    private List<Role> getRoles(IUser user) {
        Optional<Role> ou = roleRepository.findByName("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        if (ou.isPresent()) {
            roles.add(ou.orElseThrow());
        }

        if (user.isAdmin()) {
            Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
            if (oa.isPresent()) {
                roles.add(oa.orElseThrow());
            }
        }
        return roles;
    }

}