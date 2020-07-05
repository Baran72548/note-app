package com.barmej.mynote.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.databinding.ItemCheckListBinding;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;
import java.util.List;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {


    //private ArrayList<CheckItem> mItems;
    private List<CheckItem> mItems;
    private CheckBoxClickListener mCheckBoxClickListener;

    public ChecklistAdapter(List<CheckItem> mItems, CheckBoxClickListener mCheckBoxClickListener) {
        this.mItems = mItems;
        this.mCheckBoxClickListener = mCheckBoxClickListener;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_list, parent, false);
        ItemCheckListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_check_list, parent, false);
        return new ChecklistViewHolder(binding, mCheckBoxClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        CheckItem checkItem = mItems.get(position);
        holder.binding.setCheckItem(checkItem);
        //holder.checkBox.setText(checkItem.getCheckBoxItemText());
//        holder.checkBox.setChecked(checkItem.getCheckBoxItemStatus());
//        if (checkItem.getCheckBoxItemStatus()) {
//            //holder.checkBox.setChecked(true);
//            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }

    }

//    /**
//     * Add ClickListener to checkbox after creating the view to be able to update "setChecked()" in onBindViewHolder().
//     */
//    @Override
//    public void onViewAttachedToWindow(@NonNull final ChecklistViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                int checkBoxPosition = holder.getAdapterPosition();
//                if(isChecked) {
//                    holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    //mItems.get(checkBoxPosition).setCheckBoxItemStatus(false);
//                    holder.binding.setCheckItem(mItems.get(checkBoxPosition));
//                    holder.checkBox.setChecked(mItems.get(checkBoxPosition).getCheckBoxItemStatus());
//                } else {
//                    if ((holder.checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
//                        holder.checkBox.setPaintFlags( holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                    }
//                    holder.binding.setCheckItem(mItems.get(checkBoxPosition));
//                    holder.checkBox.setChecked(mItems.get(checkBoxPosition).getCheckBoxItemStatus());
//                    //mItems.get(checkBoxPosition).setCheckBoxItemStatus(true);
//                }
//                mCheckBoxClickListener.onCheckBoxClickListener(checkBoxPosition, isChecked);
//                holder.binding.setCheckItem(mItems.get(checkBoxPosition));
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ChecklistViewHolder extends RecyclerView.ViewHolder {

        private final ItemCheckListBinding binding;

        public ChecklistViewHolder(ItemCheckListBinding binding, final CheckBoxClickListener mCheckBoxClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.noteCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckBoxClickListener.onCheckBoxClickListener(getAdapterPosition(), ((CheckBox)v).isChecked());
                }
            });
        }

    }


    public CheckItem getItemAtPosition(int position) {
        return mItems.get(position);
    }

}
