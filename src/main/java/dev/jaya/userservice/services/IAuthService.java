package dev.jaya.userservice.services;

import dev.jaya.userservice.models.User;

public interface IAuthService {
    public User signup(String name, String password, String email);
}
