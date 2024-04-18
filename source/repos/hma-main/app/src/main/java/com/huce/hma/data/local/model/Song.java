package com.huce.hma.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.huce.hma.common.utils.TimestampConverter;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey    @Getter @Setter

    @NonNull public Long Id;
    public Long LyricId;
    public String Artist;
    @Getter @Setter
    public String thumbnailPath;
    @Getter @Setter
    public String Name;
    public String FilePath;
    @TypeConverters({TimestampConverter.class})
    public String CreatedAt;
    @TypeConverters({TimestampConverter.class})
    public String UpdatedAt;
}
