package dev.jaya.userservice.controllers;

import dev.jaya.userservice.dtos.LoginDTO;
import dev.jaya.userservice.dtos.RequestUserDTO;
import dev.jaya.userservice.dtos.ResponseUserDTO;
import dev.jaya.userservice.models.User;
import dev.jaya.userservice.services.AuthService;
import dev.jaya.userservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RequestUserDTO requestUser){
        try {
            User user = authService.signup(requestUser.getName(), requestUser.getEmail(), requestUser.getPassword());
            ResponseUserDTO responseUserDTO = user.toDTO();
            return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
