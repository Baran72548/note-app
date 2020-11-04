package com.barmej.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.barmej.mynote.adapter.ChecklistAdapter;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.viewmodel.MainViewModel;
import com.barmej.mynote.databinding.ActivityEditNoteBinding;
import com.barmej.mynote.listener.CheckBoxClickListener;
import com.barmej.mynote.listener.OnNoteGetListener;

import java.util.ArrayList;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
    EditText mEditedNoteET;
    ImageView mEditedPhotoIV;
    Uri mSelectedPhotoUri = null;
    EditText mAddNewChecklistItemET;

    int mBackgroundColorId;

    long mNoteId;

    private RadioGroup mRadioGroup;
    private CardView mCardView;

    private static final int VIEW_PHOTO = 110;
    private static final int PICK_IMAGE = 130;

    LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private ChecklistAdapter mChecklistAdapter;
    //private ArrayList<CheckItem> mItems;
    private List<CheckItem> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    private MainViewModel mMainViewModel;
    private Note note;
    private CheckItem checkItem;

    private ActivityEditNoteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note);

        note = new Note();
        checkItem = new CheckItem();
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Intent intent = getIntent();
        mNoteId = intent.getLongExtra(Constants.EXTRA_NOTE_ID, 0);


        mEditedNoteET = findViewById(R.id.edit_note_edit_text);
        mEditedPhotoIV = findViewById(R.id.edit_note_image_view);

        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);

        mLinearLayout = findViewById(R.id.checklist_layout);

        findViewById(R.id.edit_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(mNoteId);
            }
        });

        mEditedPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewPhoto(mSelectedPhotoUri);
                viewPhoto();
            }
        });

        findViewById(R.id.photo_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhotoIntent();
            }
        });

        findViewById(R.id.checkbox_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checkboxes for list items are hidden. If user need to add a checklist item they need to click on
                // the 'checkbox_image_button'.
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setCardViewColor();
                setNoteDataBinding();
            }
        });

        //In case of adding a new list item, we need to make a recyclerView to display items in it.
        mRecyclerView = findViewById(R.id.checklist_recycler_view);
        mItems = new ArrayList<>();
        mChecklistAdapter = new ChecklistAdapter(new CheckBoxClickListener() {
            @Override
            public void onCheckBoxClickListener(CheckItem checkItem, boolean checked) {
                onCheckListItemClicked(checkItem, checked);
            }
        });

        mListLayoutManager = new WrapContentLinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mListLayoutManager);
        mRecyclerView.setAdapter(mChecklistAdapter);

        // Take data from EditText and add it to the check list (ArrayList<Note> getChecklistList()).
        mAddNewChecklistItemET = findViewById(R.id.add_checklist_edit_text);
        findViewById(R.id.add_new_checklist_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChecklistItem(mNoteId);
            }
        });

        editedNote(mNoteId);
    }

    /**
     * Setting note's data for binding.
     */
    private void setNoteDataBinding() {
        note.setNoteText(mEditedNoteET.getText().toString());
        note.setNotePhotoUri(mSelectedPhotoUri);
        note.setCheckItem(mItems);
        note.setNoteColorId(mBackgroundColorId);
        mBinding.setNote(note);
    }

    private void pickPhotoIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    /**
     * تغيير المسمى كالدالة السابقة
     * Get note's info from database based on selected note's Id.
     */
    private void editedNote(final long noteId) {
        note = mMainViewModel.getNoteInfo(noteId);

        mEditedNoteET.setText(note.getNoteText());

        Log.i("Edited Note", String.valueOf(note.getNoteText()));

        mSelectedPhotoUri = note.getNotePhotoUri();

        /** m. */
        mMainViewModel.getNoteCheckList(noteId).observe(this, new Observer<List<CheckItem>>() {
            @Override
            public void onChanged(List<CheckItem> checkItems) {
                mChecklistAdapter.submitList(checkItems);
                mItems.addAll(checkItems);
            }
        });

                switch (note.getNoteColorId()) {
                    case R.color.white:
                        mRadioGroup.check(R.id.white_radio_button);
                        break;
                    case R.color.blue:
                        mRadioGroup.check(R.id.blue_radio_button);
                        break;
                    case R.color.red:
                        mRadioGroup.check(R.id.red_radio_button);
                        break;
                    case R.color.yellow:
                        mRadioGroup.check(R.id.yellow_radio_button);
                        break;
                }
                mBackgroundColorId = note.getNoteColorId();
//            }
//        });

        //setNoteDataBinding();
        mBinding.setNote(note);
    }

    /**
     * This method will be called to show the added photo.
     * @param photoUri is the Uri of selected photo.
     */
    private void setSelectedPhoto(Uri photoUri) {
        mSelectedPhotoUri = photoUri;
        setNoteDataBinding();
    }

    /**
     * Setting selected color to CardView.
     */
    private void setCardViewColor() {
        int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.white_radio_button:
                mBackgroundColorId = R.color.white;
                break;
            case R.id.blue_radio_button:
                mBackgroundColorId = R.color.blue;
                break;
            case R.id.red_radio_button:
                mBackgroundColorId = R.color.red;
                break;
            case R.id.yellow_radio_button:
                mBackgroundColorId = R.color.yellow;
                break;
        }
    }

    /**
     * Start a new activity to display image in a full size.
     */
    public void viewPhoto() {
        Intent intent = new Intent(EditNoteActivity.this, ViewPhotoActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_VIEW_URI, mSelectedPhotoUri);
        startActivityForResult(intent, VIEW_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_PHOTO) {
            if (resultCode == RESULT_OK && data != null) {
                removePhoto();
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedPhoto(data.getData());
            }
        }
    }

    /**
     * This method will be called from onActivityResult (after returning from ViewPhotoActivity), if user deleted the image.
     */
    private void removePhoto() {
        mSelectedPhotoUri = null;
        setNoteDataBinding();
    }

    /**
     * Adding checkList items to Database.
     */
    public void addNewChecklistItem(long position) {
        checkItem.setNoteId(position);
        checkItem.setCheckBoxItemText(mAddNewChecklistItemET.getText().toString());
        checkItem.setCheckBoxItemStatus(false);
        mMainViewModel.addCheckItem(checkItem);

        mAddNewChecklistItemET.setText("");
    }

    /**
     * Responding to ClickListener on checkList items and update the status of checkBox item.
     * @param checkItem is the clicked item.
     * @param checkBoxStatus isChecked value of checkBox.
     */
    private void onCheckListItemClicked(CheckItem checkItem, boolean checkBoxStatus) {
        checkItem.setCheckBoxItemStatus(checkBoxStatus);

        Log.i("new status ", String.valueOf(checkItem.getCheckBoxItemStatus()));

        mMainViewModel.updateCheckItemStatus(checkItem);

        mMainViewModel.getNoteCheckList(mNoteId).observe(this, new Observer<List<CheckItem>>() {
            @Override
            public void onChanged(List<CheckItem> checkItems) {
                mChecklistAdapter.submitList(checkItems);
            }
        });
    }

    public void submit(long noteId) {
        String editedNoteText = mEditedNoteET.getText().toString();

        if (editedNoteText.isEmpty() && mSelectedPhotoUri == null && mChecklistAdapter.getItemCount() == 0) {
            mMainViewModel.deleteNote(noteId);
            finish();
        }

        note.setId(noteId);
        setNoteDataBinding();
        mMainViewModel.updateNote(note);

        finish();
    }
}
