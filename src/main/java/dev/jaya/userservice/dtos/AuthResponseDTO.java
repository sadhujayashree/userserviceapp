package dev.jaya.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    public String accesstoken;
    public String refreshtoken;
}
