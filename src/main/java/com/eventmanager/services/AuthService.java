package com.eventmanager.services;

import com.eventmanager.entities.User;
import com.eventmanager.exceptions.UnauthorizedException;
import com.eventmanager.payload.LoginDTO;
import com.eventmanager.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        User found = this.usersService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("credenziali errate");
        }
    }
}