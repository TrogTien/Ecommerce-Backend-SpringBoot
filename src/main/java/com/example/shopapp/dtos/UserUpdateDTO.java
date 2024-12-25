package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {
    @JsonProperty("full_name")
    private  String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private Integer facebookAccountId;

    @JsonProperty("google_account_id")
    private Integer googleAccountId;
}
