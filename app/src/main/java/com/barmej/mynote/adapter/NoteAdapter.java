package com.barmej.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.databinding.ItemCardViewBinding;
import com.barmej.mynote.databinding.ItemCardViewWithCheckListBinding;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    private static int NOTE_WITHOUT_CHECK_LIST_TYPE = 1;
    private static int NOTE_WITH_CHECK_LIST_TYPE = 2;

    public NoteAdapter() {}

    public NoteAdapter(List<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == NOTE_WITHOUT_CHECK_LIST_TYPE) {
            ItemCardViewBinding itemCardViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_card_view, parent, false);
            return new NoteWithoutCheckListViewHolder(itemCardViewBinding, mItemClickListener, mItemLongClickListener);
        } else {
            ItemCardViewWithCheckListBinding withCheckListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_card_view_with_check_list, parent, false);
            return new NoteWithCheckListViewHolder(withCheckListBinding, mItemClickListener, mItemLongClickListener);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {

        if (getItemViewType(position) == NOTE_WITHOUT_CHECK_LIST_TYPE) {
            holder.binding.setNote(mItems.get(position));
        } else {
            holder.withCheckListBinding.setNote(mItems.get(position));
        }

        holder.position = position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position).getCheckItem() == null) {
            return NOTE_WITHOUT_CHECK_LIST_TYPE;

        } else {
            return NOTE_WITH_CHECK_LIST_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private int position;

        private ItemCardViewBinding binding;
        private ItemCardViewWithCheckListBinding withCheckListBinding;

        public NoteViewHolder(ItemCardViewBinding binding, final ItemClickListener mItemClickListener, final ItemLongClickListener mItemLongClickListen) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClickListener(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListen.onItemLongClickListener(position);
                    return true;
                }
            });
        }

        public NoteViewHolder(ItemCardViewWithCheckListBinding withCheckListBinding, final ItemClickListener  mItemClickListener, final ItemLongClickListener mItemLongClickListen) {
            super(withCheckListBinding.getRoot());
            this.withCheckListBinding = withCheckListBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClickListener(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListen.onItemLongClickListener(position);
                    return true;
                }
            });
        }
    }

    private static class NoteWithoutCheckListViewHolder extends NoteViewHolder {
        public NoteWithoutCheckListViewHolder(ItemCardViewBinding binding, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
            super(binding, mItemClickListener, mItemLongClickListener);
        }
    }

    private static class NoteWithCheckListViewHolder extends NoteViewHolder {
        public NoteWithCheckListViewHolder(ItemCardViewWithCheckListBinding binding, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
            super(binding, mItemClickListener, mItemLongClickListener);
        }
    }

    public void updateData(List<Note> notes) {
        this.mItems = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAtPosition(int position) {
        return mItems.get(position);
    }
}
