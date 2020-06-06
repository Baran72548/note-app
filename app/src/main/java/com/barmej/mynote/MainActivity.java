package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.barmej.mynote.adapter.NoteAdapter;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.DataRepository;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.database.AppDatabase;
import com.barmej.mynote.data.viewmodel.MainViewModel;
//import com.barmej.mynote.databinding.ActivityMainBinding;
import com.barmej.mynote.listener.ItemClickListener;
import com.barmej.mynote.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    //private ArrayList<Note> mItems;
    private AppDatabase mAppDatabase;
    private DataRepository mDataRepository;
    private MainViewModel mMainViewModel;

    private Note mNote;
    private List<Note> mItems;

    private ArrayList<CheckItem> mCheckItemItems;

    Menu mMenu;

    RecyclerView.LayoutManager mListLayoutManager;
    RecyclerView.LayoutManager mStaggeredGridLayoutManager;

    private static final int ADD_NOTE = 130;
    private static final int EDIT_NOTE = 140;

    //private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAppDatabase = AppDatabase.getInstance(this);
        mDataRepository = DataRepository.getInstance(getApplicationContext());
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mCheckItemItems = CheckItem.getChecklistList();

        mRecyclerView = findViewById(R.id.recycler_view);
//        mItems = Note.getDefaultList();
        mItems = new ArrayList<>();
//        mItems = mAppDatabase.noteInfoDao().getNoteInfo();
//        mItems = mMainViewModel.getNotes();
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
        getAllNotes();

//        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startAddNewNoteActivity();
//            }
//        });
    }

    /**
     * Starting a new activity to add a new item.
     */
    public void startAddNewNoteActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
        //startActivityForResult(intent, ADD_NOTE);
        startActivity(intent);
        //getAllNotes();
    }

    /**
     * Getting the results in case of adding a new item or editing the item.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
//                String noteText = data.getStringExtra(Constants.EXTRA_NOTE_TEXT);
//                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_NOTE_PHOTO_URI);
//                ArrayList<CheckItem> checkItemItems = (ArrayList) data.getSerializableExtra(Constants.EXTRA_NOTE_CHECKLIST);
//                int backgroundColorId = data.getIntExtra(Constants.EXTRA_NOTE_COLOR_NAME, 0);
//
//                Note note = new Note(noteText, photoUri, checkItemItems, backgroundColorId);
//                addNewItem(note);

                getAllNotes();
            }
        } else if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK && data != null) {
//                String noteText = data.getStringExtra(Constants.EXTRA_NOTE_EDITING_TEXT);
//                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI);
//                ArrayList<CheckItem> checkItemItems = (ArrayList) data.getSerializableExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST);
//                int backgroundColorId = data.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
//                int position = data.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);
//
//                if (noteText.isEmpty() && photoUri == null && checkItemItems.size() == 0) {
//                        deleteItem(position);
//                } else {
//                    Note note = new Note(noteText, photoUri, checkItemItems, backgroundColorId);
//                    updateItem(note, position);
//                }

            }
        }
    }

//    /**
//     * This method will be called from onActivityResult() to add new item (text, photo, or check list).
//     * @param note will carry data of String, Uri, or ArrayList.
//     */
//    private void addNewItem(Note note) {
//        mItems.add(note);
//        mAdapter.notifyItemInserted(mItems.size() - 1);
//        mAppDatabase.noteInfoDao().addNoteInfo(note);
//    }

    private void getAllNotes() {
        //mItems =
        mMainViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.updateData(notes);
                Log.i("Adapter.. ", String.valueOf(mAdapter));
            }
        });

//        mAdapter = new NoteAdapter(mItems,
//                new ItemClickListener() {
//                    @Override
//                    public void onItemClickListener(int position) {
//                        editItem(position);
//                    }
//                },
//                new ItemLongClickListener() {
//                    @Override
//                    public void onItemLongClickListener(int position) {
//                        deleteItem(position);
//                    }
//                },
//                this);
//        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * This method will be called in case of click on a recyclerView item.
     * @param position will be needed to save the position of an item on the list.
     */
    private void editItem(int position) {
//        Note note = mItems.get(position);
//        String noteText = note.getNoteText();
//        Uri photoUri = note.getNotePhoto();
//        ArrayList<CheckItem> checkItemItems = note.getCheckItem();
//        int backgroundColorId = note.getNoteColorId();
//
//        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_TEXT, noteText);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI, photoUri);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST, checkItemItems);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_COLOR, backgroundColorId);
//        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
//        startActivityForResult(intent, EDIT_NOTE);

        Note note = mAdapter.getNoteAtPosition(position);
        int noteId = note.getId();
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }

    /**
     * This method will be called from onActivityResult() to update item data (text, photo, or check list)
     * in specific position.
     * @param note will carry updated data of String, Uri, or ArrayList.
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
                        mNote = mAdapter.getNoteAtPosition(position);
                        mDataRepository.deleteNote(mNote.getId());
                        Log.i("delete note", "note deleted" + mNote);
//                        mItems.remove(position);
//                        mAdapter.notifyItemRemoved(position);
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
