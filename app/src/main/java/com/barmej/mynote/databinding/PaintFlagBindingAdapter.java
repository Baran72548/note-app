package com.barmej.mynote.databinding;

import android.graphics.Paint;
import android.widget.CheckBox;

import androidx.databinding.BindingAdapter;

public class PaintFlagBindingAdapter {

    @BindingAdapter("paintFlag")
    public static void setPaintFlag(CheckBox checkBox, boolean isChecked) {
        if(isChecked) {
            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
