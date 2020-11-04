package com.barmej.mynote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.databinding.ItemCheckListBinding;
import com.barmej.mynote.diffutil.DiffUtilCallback;
import com.barmej.mynote.listener.CheckBoxClickListener;

public class ChecklistAdapter extends ListAdapter<CheckItem, ChecklistAdapter.ChecklistViewHolder> {
    private CheckBoxClickListener mCheckBoxClickListener;
    private static DiffUtilCallback diffUtilCallback = new DiffUtilCallback();

    public ChecklistAdapter(CheckBoxClickListener mCheckBoxClickListener) {
        super(diffUtilCallback);
        this.mCheckBoxClickListener = mCheckBoxClickListener;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_check_list,
                parent, false);
        return new ChecklistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        CheckItem checkItem = getItem(position);
        holder.binding.setCheckItem(checkItem);
    }

    class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private final ItemCheckListBinding binding;

        public ChecklistViewHolder(ItemCheckListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.noteCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCheckBoxClickListener.onCheckBoxClickListener(getItem(getAdapterPosition()), ((CheckBox)view).isChecked());
                }
            });
        }
    }
}
