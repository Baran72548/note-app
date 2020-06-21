package com.barmej.mynote.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.barmej.mynote.data.CheckItem;

import java.util.List;

@Dao
public interface CheckListDao {

    @Query("SELECT * FROM check_list_info where noteId = :checkListId")
    LiveData<List<CheckItem>> getCheckListItems(int checkListId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCheckItemStatus(CheckItem checkItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCheckItem(CheckItem checkItem);
}
