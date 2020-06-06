package com.barmej.mynote.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barmej.mynote.data.database.AppDatabase;

import java.util.List;

public class DataRepository {
    private static final Object LOCK = new Object();
    private static DataRepository sInstance;
    private static AppDatabase mAppDatabase;
    private MutableLiveData<Note> mNoteMutableLiveData;
    private MutableLiveData<CheckItem> mCheckItemMutableLiveData;

    public static DataRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new DataRepository(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private DataRepository(Context context) {
        mAppDatabase = AppDatabase.getInstance(context);
        mNoteMutableLiveData = new MutableLiveData<>();
        mCheckItemMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Note>> getAllNotes()  {
        return mAppDatabase.noteInfoDao().getAllNotes();
    }

    public LiveData<Note> getNoteInfo(long id) {
        return mAppDatabase.noteInfoDao().getNoteInfo(id);
    }
    public Note getNoteInfo2(long id) {
        return mAppDatabase.noteInfoDao().getNoteInfo2(id);
    }

    public long addNote(Note note) {
        return mAppDatabase.noteInfoDao().addNoteInfo(note);
    }

    public void updateNote(Note note) {
        mAppDatabase.noteInfoDao().updateNoteInfo(note);
    }

    public void deleteNote(int id) {
        mAppDatabase.noteInfoDao().deleteNoteInfo(id);
    }


    public LiveData<List<CheckItem>> getNoteCheckItems(int noteId) {
        return mAppDatabase.checkListDao().getCheckListItems(noteId);
    }

    public void updateCheckItemStatus(CheckItem checkItem) {
        mAppDatabase.checkListDao().updateCheckItemStatus(checkItem);
    }

    public void addCheckItem(CheckItem checkItem) {
        mAppDatabase.checkListDao().addCheckItem(checkItem);
    }

//    public LiveData<List<CheckItem>> getAllCheckItems() {
//        return mAppDatabase.checkListDao().getAllCheckListItems();
//    }

}
