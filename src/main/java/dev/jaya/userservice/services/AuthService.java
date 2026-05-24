package dev.jaya.userservice.services;

import dev.jaya.userservice.exceptions.UserAlreadyExistsException;
import dev.jaya.userservice.models.Role;
import dev.jaya.userservice.models.State;
import dev.jaya.userservice.models.User;
import dev.jaya.userservice.repositories.RoleRepository;
import dev.jaya.userservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
