package com.barmej.mynote.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.barmej.mynote.data.Note;

import java.util.List;

@Dao
public interface NoteInfoDao {

    @Query("SELECT * FROM note_info")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_info where id = :id LIMIT 1")
    Note getNoteInfo(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addNoteInfo(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNoteInfo(Note note);

    @Query("DELETE FROM note_info where id = :id")
    void deleteNoteInfo(long id);
}
