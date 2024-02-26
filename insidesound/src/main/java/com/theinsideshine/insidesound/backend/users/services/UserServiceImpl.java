package com.theinsideshine.insidesound.backend.users.services;

import com.theinsideshine.insidesound.backend.exceptions.InsidesoundErrorCode;
import com.theinsideshine.insidesound.backend.exceptions.InsidesoundException;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDto;
import com.theinsideshine.insidesound.backend.users.models.dto.UserRequestDtoUpdate;
import com.theinsideshine.insidesound.backend.users.models.dto.UserResponseDto;
import com.theinsideshine.insidesound.backend.users.models.entities.Role;
import com.theinsideshine.insidesound.backend.users.models.entities.User;
import com.theinsideshine.insidesound.backend.users.repositories.RoleRepository;
import com.theinsideshine.insidesound.backend.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::userResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponseDto> userResponsesDtos = userPage.getContent()
                .stream()
                .map(UserResponseDto::userResponseDtoMapperEntityToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userResponsesDtos, pageable, userPage.getTotalElements());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id).map(UserResponseDto::userResponseDtoMapperEntityToDto);
    }

    @Transactional(readOnly = true)
    public List<String> getAllUsernames() {
        Iterable<User> usersIterable = userRepository.findAll();
        List<User> usersList = StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());

        return usersList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {

        User user = UserRequestDto.UserRequestDtoMapperDtoToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(false));
        User savedUser = userRepository.save(user);
        return UserResponseDto.userResponseDtoMapperEntityToDto(savedUser);
    }


    @Override
    @Transactional
    public UserResponseDto update(UserRequestDtoUpdate userRequestDtoUpdate, Long id) {
        User userToUpdate = validateUserIdPost(id);

        // Actualizar los campos del usuario con los valores del DTO
        userToUpdate.setUsername(userRequestDtoUpdate.username());
        userToUpdate.setEmail(userRequestDtoUpdate.email());

        //Actualiza el rol con el DTO
        userToUpdate.setRoles(getRoles(userRequestDtoUpdate.admin()));

        // Guardar el usuario actualizado en la base de datos
        try {
            User updatedUser = userRepository.save(userToUpdate);
            return UserResponseDto.userResponseDtoMapperEntityToDto(updatedUser);
        } catch (Exception e) {
            // Manejar la excepción según la lógica de tu aplicación
            throw new InsidesoundException(InsidesoundErrorCode.ERR_UPDATING_USER);
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {

        Optional<User> o = userRepository.findById(id);
        if (o.isPresent()) {
            try {
                userRepository.deleteById(id);
            } catch (Exception e) {
                throw new InsidesoundException(InsidesoundErrorCode.ERR_DEL_USER);
            }
        }

    }

    private List<Role> getRoles(boolean isAdmin) {
        List<Role> roles = new ArrayList<>();

        if (isAdmin) {
            // Si el usuario es administrador, agregamos el rol de administrador
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        } else {
            // Si el usuario no es administrador, solo agregamos el rol de usuario
            Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
            userRoleOptional.ifPresent(roles::add);
        }

        return roles;
    }

    private User validateUserIdPost(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new InsidesoundException(InsidesoundErrorCode.ID_USER_NOT_FOUND);
        }
        return optionalUser.get();
    }

}