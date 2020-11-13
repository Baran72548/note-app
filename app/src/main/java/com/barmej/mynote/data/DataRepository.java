package com.barmej.mynote.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.barmej.mynote.listener.OnNoteAddListener;
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
    public void addNote(Note note, OnNoteAddListener onNoteAddListener) {
        new addAsyncTask(mNoteInfoDao, onNoteAddListener).execute(note);
    }

    private static class addAsyncTask extends AsyncTask<Note, Void, Long> {
        private NoteInfoDao noteInfoDao;
        private OnNoteAddListener onNoteAddListener;

        public addAsyncTask(NoteInfoDao noteInfoDao, OnNoteAddListener onNoteAddListener) {
            this.noteInfoDao = noteInfoDao;
            this.onNoteAddListener = onNoteAddListener;
        }

        @Override
        protected Long doInBackground(Note... notes) {
            return noteInfoDao.addNoteInfo(notes[0]);
        }

        @Override
        protected void onPostExecute(Long noteId) {
            onNoteAddListener.onNoteAdded(noteId);
        }
    }

    /**
     * Update note info.
     * @param note is the new data to be updated.
     */
    public void updateNote(Note note) {
        new updateAsyncTask(mNoteInfoDao).execute(note);
    }

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
     * @param id of note which will be deleted.
     */
    public void deleteNote(long id) {
        new deleteAsyncTask(mNoteInfoDao).execute(id);
    }

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
    public void updateCheckItemStatus(CheckItem checkItem) {
        new updateCheckItemAsyncTask(mCheckListDao).execute(checkItem);
    }

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
     * @param checkItem to be added to the database.
     */
    public void addCheckItem(CheckItem checkItem) {
        new addCheckItemAsyncTask(mCheckListDao).execute(checkItem);
    }

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
