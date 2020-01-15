package com.barmej.mynote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.mynote.R;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    public NoteAdapter(ArrayList<Note> mItems, ItemClickListener mItemClickListener, ItemLongClickListener mItemLongClickListener) {
        this.mItems = mItems;
        this.mItemClickListener = mItemClickListener;
        this.mItemLongClickListener = mItemLongClickListener;
    }

    private Random mRandom = new Random();

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new NoteViewHolder(view, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = mItems.get(position);
        holder.noteTextTV.setText(note.getNoteText());
        holder.notePhotoIV.setImageURI(note.getNotePhoto());
        //holder.noteTextTV.getLayoutParams().height = getRandomRange(280, 75);
        holder.position = position;
    }

    /**
     * This method will create a random integer values of width and high for Textview's holder dimensions
     * in order to get different ratios and to not show all note text.
     */
    private int getRandomRange(int max, int min) {
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

        public NoteViewHolder(@NonNull View itemView, final ItemClickListener mItemClickListener, final ItemLongClickListener mItemLongClickListen) {
            super(itemView);
            noteTextTV = itemView.findViewById(R.id.item_note_text_view);
            notePhotoIV = itemView.findViewById(R.id.item_note_image_view);
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
