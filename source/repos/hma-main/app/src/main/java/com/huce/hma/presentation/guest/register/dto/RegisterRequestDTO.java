package com.huce.hma.presentation.guest.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String username;
    private String password;

}
