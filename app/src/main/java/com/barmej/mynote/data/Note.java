package com.barmej.mynote.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.barmej.mynote.R;

import java.util.ArrayList;

public class Note implements Parcelable {
    private String noteText;
    private Uri notePhoto;
    private ArrayList<CheckList> checkList;
    private int noteColorId;

    public Note(String noteText, int noteColorId) {
        this.noteText = noteText;
        this.noteColorId = noteColorId;
    }

    public Note(String noteText, Uri notePhoto, int noteColorId) {
        this.noteText = noteText;
        this.notePhoto = notePhoto;
        this.noteColorId = noteColorId;
    }

    public Note(String noteText, Uri notePhoto, ArrayList<CheckList> checkList, int noteColorId) {
        this.noteText = noteText;
        this.notePhoto = notePhoto;
        this.checkList = checkList;
        this.noteColorId = noteColorId;
    }

    public String getNoteText() {
        return noteText;
    }

    public Uri getNotePhoto() {
        return notePhoto;
    }

    public ArrayList<CheckList> getNoteCheckList() {
        return checkList;
    }

    public int getNoteColorId() {
        return noteColorId;
    }

    // This is the main ArrayList which has all notes with their different types (text, photo, or check list).
    public  static ArrayList<Note> getDefaultList() {
        ArrayList<Note> defaultList = new ArrayList<>();
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        defaultList.add(new Note("مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك. مرحباً بك في My Note. بإمكانك إضافة ملاحظاتك هنا، كما يمكنك إضافة بعض الصور أو إضافة قائمتك.", R.color.white));
        return defaultList;
    }



    //write object values to parcel for storage
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(noteText);
    }

    //constructor used for parcel
    public Note(Parcel parcel) {
        noteText = parcel.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}
