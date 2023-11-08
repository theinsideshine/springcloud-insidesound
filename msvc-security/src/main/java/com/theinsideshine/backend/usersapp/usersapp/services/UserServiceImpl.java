package com.theinsideshine.backend.usersapp.usersapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.theinsideshine.backend.usersapp.usersapp.clients.AlbumClientRest;
import com.theinsideshine.backend.usersapp.usersapp.clients.TrackClientRest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.theinsideshine.backend.usersapp.usersapp.models.IUser;
import com.theinsideshine.backend.usersapp.usersapp.models.dto.UserDto;
import com.theinsideshine.backend.usersapp.usersapp.models.dto.mapper.DtoMapperUser;
import com.theinsideshine.backend.usersapp.usersapp.models.entities.Role;
import com.theinsideshine.backend.usersapp.usersapp.models.entities.User;
import com.theinsideshine.backend.usersapp.usersapp.models.request.UserRequest;
import com.theinsideshine.backend.usersapp.usersapp.repositories.RoleRepository;
import com.theinsideshine.backend.usersapp.usersapp.repositories.UserRepository;

import javax.sound.midi.Track;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TrackClientRest trackClientRest;

    @Autowired
    private AlbumClientRest albumClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(u -> DtoMapperUser.builder().setUser(u).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(u -> DtoMapperUser.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
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
    public UserDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles( user));
        return DtoMapperUser.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, Long id) {
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


        // Llama al cliente Feign de "tracks" para poner borrar los track con el username
        ResponseEntity<?> trackResponse = trackClientRest.removeTracksByUsername(username);;

        if (trackResponse.getStatusCode().is5xxServerError()) {

            throw new EntityNotFoundException("Error al borrar tracks con el username"); // Hubo error en el servidor
        }

        // Llama al cliente Feign de "album" para poner borrar los track con el username
        ResponseEntity<?> albumResponse = albumClientRest.removeAlbumsByUsername(username);;

        if (albumResponse.getStatusCode().is5xxServerError()) {

            throw new EntityNotFoundException("Error al borrar albumes con el username"); // Hubo error en el servidor
        }

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