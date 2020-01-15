package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.barmej.mynote.adapter.NoteAdapter;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<Note> mItems;

    Menu mMenu;

    RecyclerView.LayoutManager mListLayoutManager;
    RecyclerView.LayoutManager mStaggeredGridLayoutManager;

    private static final int ADD_NOTE = 130;
    private static final int EDIT_NOTE = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mItems = Note.getDefaultList();
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


        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startAddNewNoteActivity();
            }
        });
    }

    private void startAddNewNoteActivity() {
        Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
                String noteText = data.getStringExtra(Constants.EXTRA_NOTE_TEXT);
                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_NOTE_PHOTO_URI);
                Note note = new Note(noteText, photoUri);
                addNewItem(note);
            }
        } else if (requestCode == EDIT_NOTE) {
            Log.v("noteText", "requestCode is: " + requestCode);
            if (resultCode == RESULT_OK && data != null) {
                Note note = data.getParcelableExtra(Constants.EXTRA_NOTE_EDITING);
                int position = data.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);
                updateItem(note, position);
            }
        } else {
            Toast.makeText(this, "aah ooh", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewItem(Note note) {
        mItems.add(note);
        mAdapter.notifyItemInserted(mItems.size() - 1);
    }

    private void editItem(int position) {
        Note note = mItems.get(position);
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING, note);
        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
        startActivityForResult(intent, EDIT_NOTE);
    }

    private void updateItem(Note note, int position) {
        mItems.set(position, note);
        mAdapter.notifyItemChanged(position);
    }

    private void deleteItem(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mItems.remove(position);
                        mAdapter.notifyItemRemoved(position);
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
