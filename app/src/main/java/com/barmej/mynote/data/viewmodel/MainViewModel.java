package com.barmej.mynote.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.DataRepository;
import com.barmej.mynote.data.Note;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private DataRepository mDataRepository;
//    private ArrayList<Note> notes;
    private LiveData<List<Note>> mNotes;
    private LiveData<Note> mNoteInfo;

    private LiveData<List<CheckItem>> mCheckItems;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mDataRepository = DataRepository.getInstance(application);
        mNotes = mDataRepository.getAllNotes();
        //mCheckItems = mDataRepository.getAllCheckItems();
    }

//    public ArrayList<Note> getNotes() {
//        return notes;
//    }

    public LiveData<List<Note>> getNotes() {
        return mNotes;
    }

    public LiveData<Note> getNoteInfo(long id) {
        mNoteInfo = mDataRepository.getNoteInfo(id);
        return mNoteInfo;
    }
    public Note getNoteInfo2(long id) {
        Note noteInfo = mDataRepository.getNoteInfo2(id);
        return noteInfo;
    }

    public long addNoteInfo(Note note) {
        return mDataRepository.addNote(note);
    }

    public void updateNote(Note note) {
        mDataRepository.updateNote(note);
    }

    public void deleteNote(int id) {
        mDataRepository.deleteNote(id);
    }


    public LiveData<List<CheckItem>> getNoteCheckList(int noteId) {
        return mDataRepository.getNoteCheckItems(noteId);
    }

    public void updateCheckItemStatus(CheckItem checkItem) {
        mDataRepository.updateCheckItemStatus(checkItem);
    }

    public void addCheckItem(CheckItem checkItem) {
        mDataRepository.addCheckItem(checkItem);
    }
}
