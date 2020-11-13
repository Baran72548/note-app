package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.barmej.mynote.adapter.NoteAdapter;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.viewmodel.MainViewModel;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private MainViewModel mMainViewModel;

    private Note mNote;
    private List<Note> mItems;

    Menu mMenu;

    RecyclerView.LayoutManager mListLayoutManager;
    RecyclerView.LayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mRecyclerView = findViewById(R.id.recycler_view);
        mItems = new ArrayList<>();
        mAdapter = new NoteAdapter(mItems,
                new ItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        editItem(position);
                    }
                },
                new ItemLongClickListener() {
                    @Override
                    public void onItemLongClickListener(int position) {
                        deleteItem(position);
                    }
                });

        mListLayoutManager = new LinearLayoutManager(this);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getAllNotes();
    }

    /**
     * Starting a new activity to add a new item.
     */
    public void startAddNewNoteActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
        startActivity(intent);
    }

    /**
     * Get all notes from database to display them in main activity.
     */
    private void getAllNotes() {
        mMainViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.updateData(notes);
            }
        });
    }

    /**
     * This method will be called in case of click on a recyclerView item, and start EditNoteActivity with sending note's id.
     * @param position will be needed to get note's info in that position and edit it.
     */
    private void editItem(int position) {
        Note note = mAdapter.getNoteAtPosition(position);
        long noteId = note.getId();
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }

    /**
     * This method will be called in case of long click on a recyclerView item.
     * @param position is the index of item to get it and delete it from database.
     */
    private void deleteItem(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mNote = mAdapter.getNoteAtPosition(position);
                        mMainViewModel.deleteNote(mNote.getId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.list_type_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_list){
            mRecyclerView.setLayoutManager(mListLayoutManager);
            item.setVisible(false);
            mMenu.findItem(R.id.action_staggered_grid).setVisible(true);
            return true;
        } else if (item.getItemId() == R.id.action_staggered_grid) {
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        item.setVisible(false);
        mMenu.findItem(R.id.action_list).setVisible(true);
        return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
