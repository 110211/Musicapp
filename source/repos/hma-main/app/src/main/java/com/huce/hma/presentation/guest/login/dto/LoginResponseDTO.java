package com.huce.hma.presentation.guest.login.dto;

import com.huce.hma.data.common.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDTO {
    private User user;
    private String token;
}
