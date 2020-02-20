package com.barmej.mynote.data;

import android.net.Uri;

import com.barmej.mynote.R;

import java.util.ArrayList;

public class Note {
    private String noteText;
    private Uri notePhoto;
    private ArrayList<CheckItem> checkItem;
    private int noteColorId;

    public Note(String noteText, int noteColorId) {
        this.noteText = noteText;
        this.noteColorId = noteColorId;
    }

    public Note(String noteText, Uri notePhoto, ArrayList<CheckItem> checkItem, int noteColorId) {
        this.noteText = noteText;
        this.notePhoto = notePhoto;
        this.checkItem = checkItem;
        this.noteColorId = noteColorId;
    }

    public String getNoteText() {
        return noteText;
    }

    public Uri getNotePhoto() {
        return notePhoto;
    }

    public ArrayList<CheckItem> getNoteCheckList() {
        return checkItem;
    }

    public int getNoteColorId() {
        return noteColorId;
    }

    // This is the main ArrayList which has all notes with their different types (text, photo, or check list).
    public  static ArrayList<Note> getDefaultList() {
        ArrayList<Note> defaultList = new ArrayList<>();
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.yellow));
        return defaultList;
    }
}
