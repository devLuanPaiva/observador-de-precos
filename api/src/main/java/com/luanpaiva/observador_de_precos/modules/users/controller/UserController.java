package com.luanpaiva.observador_de_precos.modules.users.controller;

import com.luanpaiva.observador_de_precos.modules.users.dto.UserResponseDTO;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public UserResponseDTO me(
            @AuthenticationPrincipal User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}