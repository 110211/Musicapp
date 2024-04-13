package com.huce.hma.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionDTO {
    private Long Id;

    private String FileName;

    private long FileData;

    private int Version;

    private String FilePath;
}
