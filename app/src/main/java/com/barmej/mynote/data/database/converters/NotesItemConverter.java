package com.barmej.mynote.data.database.converters;

import android.net.Uri;

import androidx.room.TypeConverter;

public class NotesItemConverter {

    @TypeConverter
    public static Uri UriConverterFromString(String value) {
        if (value == null) {
            return null;
        } else {
            Uri uri = Uri.parse(value);
            return uri;
        }
    }

    @TypeConverter
    public static String UriConverterFromUri(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            String stringUri = uri.toString();
            return stringUri;
        }
    }
}
