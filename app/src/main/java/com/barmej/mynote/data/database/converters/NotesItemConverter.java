package com.barmej.mynote.data.database.converters;

import android.net.Uri;

import androidx.room.TypeConverter;

public class NotesItemConverter {

    @TypeConverter
    public static Uri UriConverterFromString(String value) {
//        Type type = new TypeToken<Uri>(){}.getType();
//        return new Gson().fromJson(value, type);

        if (value == null) {
            return null;
        } else {
            Uri uri = Uri.parse(value);
            return uri;
        }
    }

    @TypeConverter
    public static String UriConverterFromUri(Uri uri) {
//        Type type = new TypeToken<Uri>(){}.getType();
//        return new Gson().toJson(uri, type);

        if (uri == null) {
            return null;
        } else {
            String stringUri = uri.toString();
            return stringUri;
        }
    }
}
