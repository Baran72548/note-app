package com.barmej.mynote.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.database.converters.CheckItemsConverter;
import com.barmej.mynote.data.database.converters.NotesItemConverter;
import com.barmej.mynote.data.database.dao.CheckListDao;
import com.barmej.mynote.data.database.dao.NoteInfoDao;

@Database(entities = {Note.class, CheckItem.class}, version = 5, exportSchema = false)
@TypeConverters({NotesItemConverter.class, CheckItemsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "notes_db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return sInstance;
    }

    public abstract NoteInfoDao noteInfoDao();
    public abstract CheckListDao checkListDao();
}
