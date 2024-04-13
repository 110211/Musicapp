package com.huce.hma.presentation.app.home.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private int Id;
    private String Username;
    private String FullName;
    private String Email;
}
