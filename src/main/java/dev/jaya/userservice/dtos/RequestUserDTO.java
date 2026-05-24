package dev.jaya.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserDTO {
    private String name;
    private String email;
    private String password;
}
