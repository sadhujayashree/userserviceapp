package dev.jaya.userservice.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Role extends BaseModel{
    @NotBlank(message = "Name shouldn't be blank")
    private String name;
}
