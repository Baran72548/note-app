package com.barmej.mynote.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.barmej.mynote.listener.OnNoteAddListener;
import com.barmej.mynote.data.database.AppDatabase;
import com.barmej.mynote.data.database.dao.CheckListDao;
import com.barmej.mynote.data.database.dao.NoteInfoDao;
import com.barmej.mynote.utils.AppExecutor;

import java.util.List;

public class DataRepository {
    private static final Object LOCK = new Object();
    private static DataRepository sInstance;
    private static AppDatabase mAppDatabase;
    private final AppExecutor mAppExecutor;

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
        mAppExecutor = AppExecutor.getsInstance();
    }

    /**
     * .. Note ..
     */

    /**
     * Get all notes from database.
     * @return list of notes.
     */
    public LiveData<List<Note>> getAllNotes()  {
        return mAppDatabase.noteInfoDao().getAllNotes();
    }

    /**
     * Get one note info from database.
     * @param id is note's id which will be returned.
     * @return note's info.
     */
    public LiveData<Note> getNoteInfo(long id) {
        return mAppDatabase.noteInfoDao().getNoteInfo(id);
    }

    /**
     * Insert note info into database.
     * @param note to be added.
     * @param onNoteAddListener will return id of inserted note.
     */
    public void addNote(final Note note, final OnNoteAddListener onNoteAddListener) {
        mAppExecutor.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                onNoteAddListener.onNoteAdded(mAppDatabase.noteInfoDao().addNoteInfo(note));
            }
        });
    }

    /**
     * Update note info.
     * @param note is the new data to be updated.
     */
    public void updateNote(final Note note) {
        mAppExecutor.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.noteInfoDao().updateNoteInfo(note);
            }
        });
    }

    /**
     * Delete note info from database task.
     * @param id of note which will be deleted.
     */
    public void deleteNote(final long id) {
        mAppExecutor.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.noteInfoDao().deleteNoteInfo(id);
            }
        });
    }


    /**
     * .. CheckList ..
     */

    /**
     * Get list of check items from database.
     * @param noteId to return only check items of selected note.
     * @return list of checkItem.
     */
    public LiveData<List<CheckItem>> getNoteCheckItems(long noteId) {
        return mAppDatabase.checkListDao().getCheckListItems(noteId);
    }

    /**
     * Update checkList item status task.
     * @param checkItem to be updated.
     */
    public void updateCheckItemStatus(final CheckItem checkItem) {
        mAppExecutor.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.checkListDao().updateCheckItemStatus(checkItem);
            }
        });
    }

    /**
     * Insert checklist item info into database task.
     * @param checkItem to be added to the database.
     */
    public void addCheckItem(final CheckItem checkItem) {
        mAppExecutor.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.checkListDao().addCheckItem(checkItem);
            }
        });
    }
}
