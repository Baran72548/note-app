package com.barmej.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.databinding.ItemCardViewBinding;
import com.barmej.mynote.databinding.ItemCardViewWithCheckListBinding;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    //private ArrayList<Note> mItems;
    private List<Note> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    private static int NOTE_WITHOUT_CHECK_LIST_TYPE = 1;
    private static int NOTE_WITH_CHECK_LIST_TYPE = 2;

//    public NoteAdapter(ArrayList<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener, Context context) {
//        this.mItems = mItems;
//        this.mItemClickListener = mItemClickListener;
//        this.mItemLongClickListener = mItemLongClickListener;
//        this.context = context;
//    }

    public NoteAdapter() {}

    public NoteAdapter(List<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener, Context context) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == NOTE_WITHOUT_CHECK_LIST_TYPE) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
//            return new NoteWithoutCheckListViewHolder(view, mItemClickListener, mItemLongClickListener);
            ItemCardViewBinding itemCardViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_card_view, parent, false);
            return new NoteWithoutCheckListViewHolder(itemCardViewBinding, mItemClickListener, mItemLongClickListener);
        } else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view_with_check_list, parent, false);
//            return new NoteWithCheckListViewHolder(view, mItemClickListener, mItemLongClickListener);
            ItemCardViewWithCheckListBinding withCheckListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_card_view_with_check_list, parent, false);
            return new NoteWithCheckListViewHolder(withCheckListBinding, mItemClickListener, mItemLongClickListener);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        //Note note = mItems.get(position);

        //holder.cardView.setBackgroundColor(context.getResources().getColor(note.getNoteColorId()));

        if (getItemViewType(position) == NOTE_WITHOUT_CHECK_LIST_TYPE) {
            //((NoteWithoutCheckListViewHolder) holder).setNoteDetails(note);
            holder.binding.setNote(mItems.get(position));
        } else {
            //((NoteWithCheckListViewHolder) holder).setCheckNoteDetails(note);
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
        TextView noteTextTV;
        ImageView notePhotoIV;
        CheckBox noteCheckBox1;
        CheckBox noteCheckBox2;
        CheckBox noteCheckBox3;
        private CardView cardView;
        //ArrayList<CheckItem> checkBoxList;
        List<CheckItem> checkBoxList;

        private ItemCardViewBinding binding;
        private ItemCardViewWithCheckListBinding withCheckListBinding;

        public NoteViewHolder(ItemCardViewBinding binding, final ItemClickListener mItemClickListener, final ItemLongClickListener mItemLongClickListen) {
            super(binding.getRoot());
            this.binding = binding;
            noteTextTV = itemView.findViewById(R.id.item_note_text_view);
            notePhotoIV = itemView.findViewById(R.id.item_note_image_view);
            cardView = itemView.findViewById(R.id.activity_card_view_background_color);

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
            noteTextTV = itemView.findViewById(R.id.item_note_text_view);
            notePhotoIV = itemView.findViewById(R.id.item_note_image_view);
            noteCheckBox1 = itemView.findViewById(R.id.item_note_checkbox_1);
            noteCheckBox2 = itemView.findViewById(R.id.item_note_checkbox_2);
            noteCheckBox3 = itemView.findViewById(R.id.item_note_checkbox_3);
            cardView = itemView.findViewById(R.id.activity_card_view_background_color);

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

//        public void setNoteDetails(Note note) {
//            noteTextTV.setText(note.getNoteText());
//
//            notePhotoIV.setImageURI(note.getNotePhotoUri());
//            if (note.getNotePhotoUri() != null) {
//                notePhotoIV.setVisibility(View.VISIBLE);
//            }
//        }
    }

    private static class NoteWithCheckListViewHolder extends NoteViewHolder {
        public NoteWithCheckListViewHolder(ItemCardViewWithCheckListBinding binding, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
            super(binding, mItemClickListener, mItemLongClickListener);
        }

//        public void setCheckNoteDetails(Note note) {
//            noteTextTV.setText(note.getNoteText());
//
//            notePhotoIV.setImageURI(note.getNotePhotoUri());
//            if (note.getNotePhotoUri() != null) {
//                notePhotoIV.setVisibility(View.VISIBLE);
//            }

//            checkBoxList = note.getCheckItem();
//            for (int i = 0; i < checkBoxList.size(); i++) {
//                CheckItem checkItem = checkBoxList.get(i);
//                String checkBoxItemText = checkItem.getCheckBoxItemText();
//                boolean checkBoxItemStatus = checkItem.getCheckBoxItemStatus();
//
//                switch (i) {
//                    case 0:
//                        noteCheckBox1.setText(checkBoxItemText);
//                        noteCheckBox1.setChecked(checkBoxItemStatus);
//                        noteCheckBox1.setVisibility(View.VISIBLE);
//                        if (checkBoxItemStatus) {
//                            noteCheckBox1.setPaintFlags(noteCheckBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        }
//                        break;
//                    case 1:
//                        noteCheckBox2.setText(checkBoxItemText);
//                        noteCheckBox2.setChecked(checkBoxItemStatus);
//                        noteCheckBox2.setVisibility(View.VISIBLE);
//                        if (checkBoxItemStatus) {
//                            noteCheckBox2.setPaintFlags(noteCheckBox2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        }
//                        break;
//                    case 2:
//                        noteCheckBox3.setText(checkBoxItemText);
//                        noteCheckBox3.setChecked(checkBoxItemStatus);
//                        noteCheckBox3.setVisibility(View.VISIBLE);
//                        if (checkBoxItemStatus) {
//                            noteCheckBox3.setPaintFlags(noteCheckBox3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        }
//                        break;
//                }
//            }
//        }
    }

    public void updateData(List<Note> notes) {
        this.mItems = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAtPosition(int position) {
        return mItems.get(position);
    }
}
