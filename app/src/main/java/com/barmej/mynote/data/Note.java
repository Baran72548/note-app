package com.barmej.mynote.data;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "note_info")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id = 0;
    @SerializedName("note_text")
    private String noteText;
    @SerializedName("note_photo_uri")
    private Uri notePhotoUri;
    @SerializedName("check_item")
    private List<CheckItem> checkItem;
    @SerializedName("note_color")
    private int noteColorId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Uri getNotePhotoUri() {
        return notePhotoUri;
    }

    public void setNotePhotoUri(Uri notePhotoUri) {
        this.notePhotoUri = notePhotoUri;
    }

    public List<CheckItem> getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(List<CheckItem> checkItem) {
        this.checkItem = checkItem;
    }

    public int getNoteColorId() {
        return noteColorId;
    }

    public void setNoteColorId(int noteColorId) {
        this.noteColorId = noteColorId;
    }
}
