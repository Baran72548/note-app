package com.barmej.mynote.diffutil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.barmej.mynote.data.CheckItem;

public class DiffUtilCallback extends DiffUtil.ItemCallback<CheckItem> {

    @Override
    public boolean areItemsTheSame(@NonNull CheckItem oldItem, @NonNull CheckItem newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull CheckItem oldItem, @NonNull CheckItem newItem) {
        return oldItem.getCheckBoxItemStatus() != newItem.getCheckBoxItemStatus();
    }
}
