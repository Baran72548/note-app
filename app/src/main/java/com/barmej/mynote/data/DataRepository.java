package com.barmej.mynote.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.barmej.mynote.data.database.AppDatabase;
import com.barmej.mynote.data.database.dao.CheckListDao;
import com.barmej.mynote.data.database.dao.NoteInfoDao;

import java.util.List;

public class DataRepository {
    private static final Object LOCK = new Object();
    private static DataRepository sInstance;
    private static AppDatabase mAppDatabase;
    private NoteInfoDao mNoteInfoDao;
    private CheckListDao mCheckListDao;

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
        mNoteInfoDao = mAppDatabase.noteInfoDao();
        mCheckListDao = mAppDatabase.checkListDao();
    }

    public LiveData<List<Note>> getAllNotes()  {
        return mAppDatabase.noteInfoDao().getAllNotes();
    }

    public Note getNoteInfo(long id) {
        return mAppDatabase.noteInfoDao().getNoteInfo(id);
    }

    /**
     * Insert note info into database.
     */
    public Long addNote(Note note) {
        //return new addAsyncTask(mNoteInfoDao).execute(note);
        return mAppDatabase.noteInfoDao().addNoteInfo(note);
    }

    /**
     * Update note info.
     */
    public void updateNote(Note note) {
        new updateAsyncTask(mNoteInfoDao).execute(note);
        //mAppDatabase.noteInfoDao().updateNoteInfo(note);
    }

    /**
     * Delete note info from database task.
     */
    public void deleteNote(long id) {
        new deleteAsyncTask(mNoteInfoDao).execute(id);
        //mAppDatabase.noteInfoDao().deleteNoteInfo(id);
    }

    /**
     * Insert note info int database task.
     */
    private static class addAsyncTask extends AsyncTask<Note, Void, Long> {
        private NoteInfoDao noteInfoDao;

        addAsyncTask(NoteInfoDao noteInfoDao) {
            this.noteInfoDao = noteInfoDao;
        }
        @Override
        protected Long doInBackground(Note... notes) {
            return noteInfoDao.addNoteInfo(notes[0]);
        }
    }

    /**
     * Update note info task.
     */
    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteInfoDao noteInfoDao;

        updateAsyncTask(NoteInfoDao noteInfoDao) {
            this.noteInfoDao = noteInfoDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteInfoDao.updateNoteInfo(notes[0]);
            return null;
        }
    }

    /**
     * Delete note info from database task.
     */
    private static class deleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private NoteInfoDao noteInfoDao;

        deleteAsyncTask(NoteInfoDao noteInfoDao) {
            this.noteInfoDao = noteInfoDao;
        }

        @Override
        protected Void doInBackground(Long... noteId) {
            noteInfoDao.deleteNoteInfo(noteId[0]);
            return null;
        }
    }



    public LiveData<List<CheckItem>> getNoteCheckItems(long noteId) {
        return mAppDatabase.checkListDao().getCheckListItems(noteId);
    }

    /**
     * Update checkList item status.
     */
    public void updateCheckItemStatus(CheckItem checkItem) {
        new updateCheckItemAsyncTask(mCheckListDao).execute(checkItem);
        //mAppDatabase.checkListDao().updateCheckItemStatus(checkItem);
    }

    public void addCheckItem(CheckItem checkItem) {
        new addCheckItemAsyncTask(mCheckListDao).execute(checkItem);
        //mAppDatabase.checkListDao().addCheckItem(checkItem);
    }

    /**
     * Update checkList item status task.
     */
    private static class updateCheckItemAsyncTask extends AsyncTask<CheckItem, Void, Void> {
        private CheckListDao checkListDao;

        updateCheckItemAsyncTask(CheckListDao checkListDao) {
            this.checkListDao = checkListDao;
        }
        @Override
        protected Void doInBackground(CheckItem... checkItems) {
            checkListDao.updateCheckItemStatus(checkItems[0]);
            return null;
        }
    }

    /**
     * Insert checklist item info into database task.
     */
    private static class addCheckItemAsyncTask extends AsyncTask<CheckItem, Void, Void> {
        private CheckListDao checkListDao;

        addCheckItemAsyncTask(CheckListDao checkListDao) {
            this.checkListDao = checkListDao;
        }
        @Override
        protected Void doInBackground(CheckItem... checkItems) {
            checkListDao.addCheckItem(checkItems[0]);
            return null;
        }
    }
}
