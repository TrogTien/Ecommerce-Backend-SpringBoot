package com.example.shopapp.controllers;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.dtos.UserUpdateDTO;
import com.example.shopapp.models.User;
import com.example.shopapp.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/users")
public class UserController {
     private final IUserService userService;
     private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        try {

            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(String.valueOf(errors));
            }

            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            userService.createUser(userDTO);

            return ResponseEntity.ok().body(Collections.singletonMap("message", "User registered successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());

            Locale locale = LocaleContextHolder.getLocale();
            Map<String, String> response = new HashMap<>();
            response.put("message", messageSource.getMessage("user.login.login_successfully", null, locale));
            response.put("token", token);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            }

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return ResponseEntity.ok(Map.of("roles", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateDTO userUpdateDTO,
            @AuthenticationPrincipal User user
    ) {
        try {
            if (!id.equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (userUpdateDTO.getPassword() != null && userUpdateDTO.getRetypePassword() != null && !userUpdateDTO.getPassword().equals(userUpdateDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            User updatedUser = userService.updateUser(userUpdateDTO, id);
            return ResponseEntity.ok().body(Map.of("message", "User updated successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

