package com.huce.hma.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private long id;
    private String username;
    private String email;
    private boolean isPremiumUser;
}
