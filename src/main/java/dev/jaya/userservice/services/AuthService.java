package dev.jaya.userservice.services;

import dev.jaya.userservice.configurations.JwtConfig;
import dev.jaya.userservice.dtos.AuthResponseDTO;
import dev.jaya.userservice.exceptions.UserAlreadyExistsException;
import dev.jaya.userservice.exceptions.UserNotFoundException;
import dev.jaya.userservice.models.Role;
import dev.jaya.userservice.models.Session;
import dev.jaya.userservice.models.State;
import dev.jaya.userservice.models.User;
import dev.jaya.userservice.repositories.RoleRepository;
import dev.jaya.userservice.repositories.SessionRepository;
import dev.jaya.userservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthService implements IAuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    JwtConfig jwtConfig;

    public User signup(String name, String email, String password){
        log.info("AuthService signup method called {}", email);
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            }
            User user = new User();
            user.setName(name);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setEmail(email);
            user.setCreatedAt(new Date());
            user.setModifiedAt(new Date());
            user.setState(State.ACTIVE);

            Role role = new Role();
            Optional<Role> optionalRole = roleRepository.findByName("DEFAULT");
            if (optionalRole.isEmpty()) {
                role.setName("DEFAULT");
                role.setCreatedAt(new Date());
                role.setModifiedAt(new Date());
                role.setState(State.ACTIVE);
                roleRepository.save(role);
            } else {
                role = optionalRole.get();
            }
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            return userRepository.save(user);
        }catch (Exception e){
            log.error("Error: {}" +e.getMessage());
            throw e;
        }
    }

    public AuthResponseDTO login(String email, String password) {
        log.info("AuthService login method called {}", email);
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            if (bCryptPasswordEncoder.matches(password, optionalUser.get().getPassword())) {
                String accessToken = jwtService.generateAccessToken(optionalUser.get());
                String refreshToken = jwtService.generateRefreshToken(optionalUser.get());
                AuthResponseDTO authResponseDTO = new AuthResponseDTO();
                authResponseDTO.setAccesstoken(accessToken);
                authResponseDTO.setRefreshtoken(refreshToken);
                Session session = new Session();
                session.setState(State.ACTIVE);
                session.setCreatedAt(new Date());
                session.setModifiedAt(new Date());
                session.setToken(refreshToken);
                session.setRevoked(false);
                session.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration()));
                sessionRepository.save(session);
                return authResponseDTO;

            } else {
                throw new UserNotFoundException("Wrong password");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
