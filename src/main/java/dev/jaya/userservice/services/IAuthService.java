package dev.jaya.userservice.services;

import dev.jaya.userservice.dtos.AuthResponseDTO;
import dev.jaya.userservice.models.User;

public interface IAuthService {
    public User signup(String name, String email, String password);

    public AuthResponseDTO login(String email, String password);
}
