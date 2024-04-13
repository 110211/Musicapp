package com.huce.hma.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.huce.hma.data.local.model.Song;

import java.util.List;

@Dao
public interface SongDao {
    @Insert
    public void insert(Song... songs);

    @Update
    public void update(Song... songs);

    @Delete
    public void delete(Song song);

    @Query("SELECT * FROM Songs")
    public List<Song> getAllSongs();
}
