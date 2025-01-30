package com.tcognition.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO {

    @NotBlank(message = "User contact must not be blank")
    private String userContact;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
