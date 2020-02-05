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
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {
    private ArrayList<CheckList> mItems;
    private CheckBoxClickListener mCheckBoxClickListener;

    public ChecklistAdapter(ArrayList<CheckList> mItems, CheckBoxClickListener mCheckBoxClickListener) {
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
        CheckList checkList = mItems.get(position);
        holder.checkBox.setText(checkList.getCheckListItemText());
        //holder.checkBox.setChecked(holder.checkBox.isChecked());

//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                //mItemClickListener.onItemClickListener(position);
//                if(isChecked) {
//                    holder.checkBoxStatus = true;
//                    holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                } else {
//                    holder.checkBoxStatus = false;
//                    if ((holder.checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
//                        holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                    }
//                }
//                mCheckBoxClickListener.onCheckBoxClickListener(position, holder.checkBoxStatus);
//            }
//        });

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private int position;
        CheckBox checkBox;
        boolean checkBoxStatus;

        public ChecklistViewHolder(@NonNull View itemView, final CheckBoxClickListener mCheckBoxClickListener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.note_checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int checkBoxPosition = getAdapterPosition();
                    if(isChecked) {
                        checkBoxStatus = true;
                        checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        checkBoxStatus = false;
                        if ((checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                            checkBox.setPaintFlags( checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        }
                    }
                    mCheckBoxClickListener.onCheckBoxClickListener(checkBoxPosition, isChecked);
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mCheckBoxClickListener.onCheckBoxClickListener(position, checkBoxStatus);
//                }
//            });
        }
    }
}
