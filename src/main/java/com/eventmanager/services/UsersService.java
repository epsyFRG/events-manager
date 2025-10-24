package com.eventmanager.services;

import com.eventmanager.entities.User;
import com.eventmanager.exceptions.BadRequestException;
import com.eventmanager.exceptions.NotFoundException;
import com.eventmanager.payload.NewUserDTO;
import com.eventmanager.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    public User save(NewUserDTO payload) {
        this.usersRepository.findByEmail(payload.email()).ifPresent(user -> {
                    throw new BadRequestException("l'email " + user.getEmail() + " è già in uso");
                }
        );

        User newUser = new User(payload.name(), payload.surname(), payload.email(), bcrypt.encode(payload.password()), payload.role());
        newUser.setAvatarURL("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());

        User savedUser = this.usersRepository.save(newUser);
        log.info("l'utente con id: " + savedUser.getId() + " è stato salvato correttamente");

        return savedUser;
    }

    public User findById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("l'utente con l'email " + email + " non è stato trovato"));
    }
}