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
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<Note> mItems;

    private ArrayList<CheckList> mCheckListItems;

    Menu mMenu;

    RecyclerView.LayoutManager mListLayoutManager;
    RecyclerView.LayoutManager mStaggeredGridLayoutManager;

    private static final int ADD_NOTE = 130;
    private static final int EDIT_NOTE = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCheckListItems = CheckList.getChecklistList();

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
                },
                this);


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

    /**
     * Starting a new activity to add a new item.
     */
    private void startAddNewNoteActivity() {
        Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }

    /**
     * Getting the results in case of adding a new item or editing the item.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
                String noteText = data.getStringExtra(Constants.EXTRA_NOTE_TEXT);
                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_NOTE_PHOTO_URI);
                ArrayList<CheckList> checkListItems = data.getParcelableArrayListExtra(Constants.EXTRA_NOTE_CHECKLIST);
                int backgroundColorId = data.getIntExtra(Constants.EXTRA_NOTE_COLOR_NAME, 0);

                Log.i("array items: ", "Main intent items: " + checkListItems);
                Note note = new Note(noteText, photoUri, checkListItems, backgroundColorId);
                addNewItem(note);
            }
        } else if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
                String noteText = data.getStringExtra(Constants.EXTRA_NOTE_EDITING_TEXT);
                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI);
                int backgroundColorId = data.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
                int position = data.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);

                if (noteText.isEmpty() && photoUri == null) {
                    deleteItem(position);
                } else {
                    Note note = new Note(noteText, photoUri, backgroundColorId);
                    updateItem(note, position);
                }
            }
        } else {
            Toast.makeText(this, "aah ooh", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will be called from onActivityResult() to add new item (text, photo, or check list).
     * @param note will carry data of String, Uri, or ...
     */
    private void addNewItem(Note note) {
        mItems.add(note);
        mAdapter.notifyItemInserted(mItems.size() - 1);
    }

    /**
     * This method will be called in case of click on a recyclerView item.
     * @param position will be needed to save the position of an item on the list.
     */
    private void editItem(int position) {
        Note note = mItems.get(position);
        Uri photoUri = note.getNotePhoto();
        ArrayList<CheckList> checkListItems = note.getNoteCheckList();
        Log.i("array items: ", "Main edit items: " + checkListItems);
        int backgroundColorId = note.getNoteColorId();

        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_TEXT, note);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI, photoUri);
        intent.putParcelableArrayListExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST, checkListItems);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_COLOR, backgroundColorId);
        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
        startActivityForResult(intent, EDIT_NOTE);
    }

    /**
     * This method will be called from onActivityResult() to update item data (text, photo, or check list)
     * in specific position.
     * @param note will carry updated data of String, Uri, or ...
     * @param position is the index of updated item on the list.
     */
    private void updateItem(Note note, int position) {
        mItems.set(position, note);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * This method will be called in case of long click on a recyclerView item.
     * @param position is the index of item to deleted from the list.
     */
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
