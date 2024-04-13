package com.huce.hma.presentation.guest.register.dto;

import com.huce.hma.data.common.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterResponseDTO {
    private User user;
    private String token;
}
