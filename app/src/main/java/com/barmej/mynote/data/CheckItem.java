package com.barmej.mynote.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "check_list_info", foreignKeys = @ForeignKey(entity = Note.class, parentColumns = "id",
        childColumns = "noteId", onDelete = CASCADE))
//public class CheckItem implements Serializable {
//@Entity(tableName = "check_list_info")
public class CheckItem {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("note_id")
    private long noteId = 0;
    @SerializedName("checkBox_text")
    private String checkBoxItemText;
    @SerializedName("checkBox_status")
    private boolean checkBoxItemStatus;

//    @Ignore
//    public CheckItem(String checkBoxItemText, boolean checkBoxItemStatus) {
//        this.checkBoxItemText = checkBoxItemText;
//        this.checkBoxItemStatus = checkBoxItemStatus;
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getCheckBoxItemText() {
        return checkBoxItemText;
    }

    public void setCheckBoxItemText(String checkBoxItemText) {
        this.checkBoxItemText = checkBoxItemText;
    }

    public boolean getCheckBoxItemStatus() {
        return checkBoxItemStatus;
    }

    public void setCheckBoxItemStatus(boolean checkBoxItemStatus) {
        this.checkBoxItemStatus = checkBoxItemStatus;
    }

    public static ArrayList<CheckItem> getChecklistList() {
        ArrayList<CheckItem> checklistItem = new ArrayList<>();
        return checklistItem;
    }
}
