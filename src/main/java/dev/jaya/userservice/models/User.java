package dev.jaya.userservice.models;

//import dev.jaya.userservice.dtos.UserDTO;
import dev.jaya.userservice.dtos.ResponseUserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel {
    @NotBlank(message = "Name is required")
    private String name;
    private String password;
    @Email
    private String email;
    @ManyToMany
    private List<Role> roles;

    public ResponseUserDTO toDTO() {
        ResponseUserDTO userDTO = new ResponseUserDTO();
        userDTO.setName(this.name);
        userDTO.setEmail(this.email);
        userDTO.setId(this.getId());
        userDTO.setRoles(this.roles);
        return userDTO;
    }
}
