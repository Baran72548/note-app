package com.barmej.mynote.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barmej.mynote.listener.OnNoteAddListener;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.DataRepository;
import com.barmej.mynote.data.Note;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private DataRepository mDataRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mDataRepository = DataRepository.getInstance(application);
    }

    /**
     * .. Note ..
     */

    /**
     * Get all notes.
     * @return list of notes.
     */
    public LiveData<List<Note>> getNotes() {
        return mDataRepository.getAllNotes();
    }

    /**
     * Get one note info.
     * @param id is note's id which will be returned.
     * @return note's info.
     */
    public LiveData<Note> getNoteInfo(long id) {
        return mDataRepository.getNoteInfo(id);
    }

    /**
     * Add note's info.
     * @param note to be added.
     * @param onNoteAddListener will return id of inserted note.
     */
    public void addNoteInfo(Note note, OnNoteAddListener onNoteAddListener) {
        mDataRepository.addNote(note, onNoteAddListener);
    }

    /**
     * Update note's info.
     * @param note is the new data to be updated.
     */
    public void updateNote(Note note) {
        mDataRepository.updateNote(note);
    }

    /**
     * Delete a note.
     * @param id of note which will be deleted.
     */
    public void deleteNote(long id) {
        mDataRepository.deleteNote(id);
    }

    /**
     * .. CheckList ..
     */

    /**
     * Get list of check items.
     * @param noteId to return only check items of selected note.
     * @return list of checkItem.
     */
    public LiveData<List<CheckItem>> getNoteCheckList(long noteId) {
        return mDataRepository.getNoteCheckItems(noteId);
    }

    /**
     * Update checkList status.
     * @param checkItem to be updated.
     */
    public void updateCheckItemStatus(CheckItem checkItem) {
        mDataRepository.updateCheckItemStatus(checkItem);
    }

    /**
     * Add new check item to the list.
     * @param checkItem to be added to the database.
     */
    public void addCheckItem(CheckItem checkItem) {
        mDataRepository.addCheckItem(checkItem);
    }
}
