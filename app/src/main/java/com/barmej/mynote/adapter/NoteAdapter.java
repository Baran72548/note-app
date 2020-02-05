package com.barmej.mynote.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private ArrayList<Note> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    public NoteAdapter(ArrayList<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener, Context context) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
        this.context = context;
    }

    private static Random mRandom = new Random();

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
            return new NoteViewHolder(view, mItemClickListener, mItemLongClickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = mItems.get(position);
        holder.cardView.setBackgroundColor(context.getResources().getColor(note.getNoteColorId()));

        holder.noteTextTV.setText(note.getNoteText());

        if (note.getNotePhoto() != null) {
            holder.notePhotoIV.setImageURI(note.getNotePhoto());
            holder.notePhotoIV.setVisibility(View.VISIBLE);
        }

        ArrayList<CheckList> checkBoxList = note.getNoteCheckList();
        if (checkBoxList != null) {
            for (int i = 0; i < checkBoxList.size(); i++) {
                CheckList checkList = checkBoxList.get(i);
                String checkBoxItemText = checkList.getCheckListItemText();
                boolean checkBoxItemStatus = checkList.isCheckListItemStatus();
                //CheckBox checkBoxStatus = checkList.getCheckBoxStatus();

                Log.i("r", "check value: " + checkBoxItemStatus);
                switch (i) {
                    case 0:
                        holder.noteCheckBox1.setText(checkBoxItemText);
                        holder.noteCheckBox1.setChecked(checkBoxItemStatus);
                        if(holder.noteCheckBox1.isChecked()) {
                            holder.noteCheckBox1.setPaintFlags(holder.noteCheckBox1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            if ((holder.noteCheckBox1.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                                holder.noteCheckBox1.setPaintFlags(holder.noteCheckBox1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                        }
                        holder.noteCheckBox1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.noteCheckBox2.setText(checkBoxItemText);
                        holder.noteCheckBox2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.noteCheckBox3.setText(checkBoxItemText);
                        holder.noteCheckBox3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        holder.noteTextTV.getLayoutParams().height = getRandomRange(280, 75);
        holder.position = position;
    }

    /**
     * This method will create a random integer values of width and high for TextView's holder dimensions
     * in order to get different ratios and to not show all note text.
     */
    private static int getRandomRange(int max, int min) {
        return mRandom.nextInt((max-min)+min)+min;
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

        public NoteViewHolder(@NonNull View itemView, final ItemClickListener mItemClickListener, final ItemLongClickListener mItemLongClickListen) {
            super(itemView);
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
}
