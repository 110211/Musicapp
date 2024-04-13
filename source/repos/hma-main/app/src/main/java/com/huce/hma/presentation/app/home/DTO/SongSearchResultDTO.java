package com.huce.hma.presentation.app.home.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SongSearchResultDTO implements Serializable {
    private Long Id;
    private String Name;
    private String FilePath;
    private String Thumbnail;
    private String ArtistName;
    private int isLiked;


}
