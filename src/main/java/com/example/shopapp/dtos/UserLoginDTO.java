package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @NotBlank(message = "Phone number is required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
