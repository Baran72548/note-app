package com.barmej.mynote.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {
    private ArrayList<CheckItem> mItems;
    private CheckBoxClickListener mCheckBoxClickListener;

    public ChecklistAdapter(ArrayList<CheckItem> mItems, CheckBoxClickListener mCheckBoxClickListener) {
        this.mItems = mItems;
        this.mCheckBoxClickListener = mCheckBoxClickListener;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_list, parent, false);
        return new ChecklistViewHolder(view, mCheckBoxClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        CheckItem checkItem = mItems.get(position);
        holder.checkBox.setText(checkItem.getCheckBoxItemText());
        holder.checkBox.setChecked(checkItem.getCheckBoxItemStatus());
        if (checkItem.getCheckBoxItemStatus()) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.position = position;
    }

    /**
     * Add ClickListener to checkbox after creating the view to be able to update "setChecked()" in onBindViewHolder().
     */
    @Override
    public void onViewAttachedToWindow(@NonNull final ChecklistViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int checkBoxPosition = holder.getAdapterPosition();
                if(isChecked) {
                    holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    if ((holder.checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        holder.checkBox.setPaintFlags( holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
                mCheckBoxClickListener.onCheckBoxClickListener(checkBoxPosition, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private int position;
        CheckBox checkBox;

        public ChecklistViewHolder(@NonNull View itemView, final CheckBoxClickListener mCheckBoxClickListener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.note_checkbox);
        }
    }
}
