package com.barmej.mynote.data.database.converters;

import androidx.room.TypeConverter;

import com.barmej.mynote.data.CheckItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckItemsConverter {
    @TypeConverter
    public static List<CheckItem> fromString(String value) {
        Type type = new TypeToken<List<CheckItem>>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArrayList(List<CheckItem> list) {
        Type type = new TypeToken<List<CheckItem>>(){}.getType();
        return new Gson().toJson(list, type);

//        String[] arrayListString = new String[list.size()];
//        arrayListString = list.toArray(arrayListString);
//        return arrayListString;
    }
}
