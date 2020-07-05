package com.barmej.mynote.data;

import android.net.Uri;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.barmej.mynote.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
   // @Relation(parentColumn = "id", entityColumn = "noteId")
    private List<CheckItem> checkItem;
    @SerializedName("note_color")
    private int noteColorId;
//
//    public Note(String noteText, int noteColorId) {
//        this.noteText = noteText;
//        this.noteColorId = noteColorId;
//    }

//    @Ignore
//    public Note(String noteText, Uri notePhotoUri, ArrayList<CheckItem> checkItem, int noteColorId) {
//        this.noteText = noteText;
//        this.notePhotoUri = notePhotoUri;
//        this.checkItem = checkItem;
//        this.noteColorId = noteColorId;
//    }

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

//    // This is the main ArrayList which has all notes with their different types (text, photo, or check list).
//    public  static ArrayList<Note> getDefaultList() {
//        ArrayList<Note> defaultList = new ArrayList<>();
//        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", null, null, R.color.white));
//        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", null, null, R.color.yellow));
//        return defaultList;
//    }
}
