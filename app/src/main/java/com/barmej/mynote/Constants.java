package com.barmej.mynote;

public class Constants {
    //Used for intent of new added text in note, from 'AddNewActivity'.
    public static final String EXTRA_NOTE_TEXT = "EXTRA_NOTE_TEXT_KEY";
    //Used for intent of the new added photo uri in note, from 'AddNewActivity'.
    public static final String EXTRA_NOTE_PHOTO_URI = "EXTRA_NOTE_PHOTO_URI_KEY";
    //Used for intent of the new added checkBox items in note, from 'AddNewActivity'.
    public static final String EXTRA_NOTE_CHECKLIST = "EXTRA_NOTE_CHECKLIST_RECYCLER_VIEW_KEY";
    //Used for intent of chosen background color of note, from 'AddNewActivity'.
    public static final String EXTRA_NOTE_COLOR_NAME = "EXTRA_NOTE_COLOR_NAME_KEY";
    //Used for intent of item position, from 'MainActivity' to 'EditNoteActivity'.
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID_KEY";
    //Used for intent of text to edit it, from 'MainActivity' to 'EditNoteActivity'.
    public static final String EXTRA_NOTE_EDITING_TEXT = "EXTRA_NOTE_EDITING_TEXT_KEY";
    //Used for intent of photo uri to edit it, from 'MainActivity' to 'EditNoteActivity'.
    public static final String EXTRA_NOTE_EDITING_PHOTO_URI = "EXTRA_NOTE_EDITING_PHOTO_URI_KEY";
    //Used for intent of checkList to edit it, from 'MainActivity' to 'EditNoteActivity'.
    public static final String EXTRA_NOTE_EDITING_CHECKLIST = "EXTRA_NOTE_EDITING_CHECKLIST_KEY";
    //Used for intent of note's background color to edit it, from 'MainActivity' to 'EditNoteActivity'.
    public static final String EXTRA_NOTE_EDITING_COLOR = "EXTRA_NOTE_EDITING_COLOR_key";
    //Used for intent of photo uri to view it or delete it, from 'AddNewActivity' and 'EditNoteActivity' to 'ViewPhotoActivity'.
    public static final String EXTRA_PHOTO_VIEW_URI = "EXTRA_PHOTO_VIEW_URI_KEY";
    public static final String EXTRA_NULL_PHOTO_URI = "EXTRA_NULL_PHOTO_URI";
    public static final String EXTRA_DIFFUTIL_BUNDLE = "EXTRA_DIFFUTIL_BUNDLE";
}
