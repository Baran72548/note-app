package com.barmej.mynote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.listener.ItemClickListener;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {
    private ArrayList<CheckList> mItems;
    private ItemClickListener mItemClickListener;

    public ChecklistAdapter(ArrayList<CheckList> mItems, ItemClickListener mItemClickListener) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_list, parent, false);
        return new ChecklistViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ChecklistViewHolder holder, int position) {
        CheckList checkList = mItems.get(position);
        holder.checkBox.setText(checkList.getCheckListItemText());
        //holder.checkBox.setChecked(checkList.isCheckListItemStatus());

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private int position;
        CheckBox checkBox;

        public ChecklistViewHolder(@NonNull View itemView, final ItemClickListener mItemClickListener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.note_checkbox);

//            checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(checkBox.isChecked()) {
//                        checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    } else {
//                        if ((checkBox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
//                            checkBox.setPaintFlags( checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                        }
//                    }
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClickListener(position);
                }
            });
        }
    }
}
