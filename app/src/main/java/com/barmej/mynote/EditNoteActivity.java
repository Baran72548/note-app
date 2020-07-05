package com.barmej.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
    EditText mEditedNoteET;
    ImageView mEditedPhotoIV;
    Uri mSelectedPhotoUri = null;
    EditText mAddNewChecklistItemET;

    int mBackgroundColorId;

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
        //setContentView(R.layout.activity_edit_note);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_note);

        note = new Note();
        checkItem = new CheckItem();
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Intent intent = getIntent();
        String noteText = intent.getStringExtra(Constants.EXTRA_NOTE_EDITING_TEXT);
        final Uri photoUri = intent.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI);
        ArrayList<CheckItem> checkItemItems = (ArrayList<CheckItem>) intent.getSerializableExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST);
        final int backgroundColorId = intent.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
        final long noteId = intent.getLongExtra(Constants.EXTRA_NOTE_ID, 0);

        mEditedNoteET = findViewById(R.id.edit_note_edit_text);
        mEditedPhotoIV = findViewById(R.id.edit_note_image_view);

        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);

        mLinearLayout = findViewById(R.id.checklist_layout);

        findViewById(R.id.edit_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(noteId);
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
        mItems = CheckItem.getChecklistList();
        mChecklistAdapter = new ChecklistAdapter(mItems,
                new CheckBoxClickListener() {
                    @Override
                    public void onCheckBoxClickListener(int position, boolean checkBoxStatus) {
                        onCheckListItemClicked(position, checkBoxStatus, noteId);
                    }
                });

        mListLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mListLayoutManager);
        mRecyclerView.setAdapter(mChecklistAdapter);

        // Take data from EditText and add it to the check list (ArrayList<Note> getChecklistList()).
        mAddNewChecklistItemET = findViewById(R.id.add_checklist_edit_text);
        findViewById(R.id.add_new_checklist_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChecklistItem(noteId);
            }
        });

        //editedNoteItems(noteText, photoUri, checkItemItems, backgroundColorId);
        editedNote(noteId);
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
    private void editedNote(long noteId) {
//        mMainViewModel.getNoteInfo(noteId).observe(this, new Observer<Note>() {
//            @Override
//            public void onChanged(Note note) {


        note = mMainViewModel.getNoteInfo(noteId);
        mSelectedPhotoUri = note.getNotePhotoUri();

//                mEditedNoteET.setText(note.getNoteText());

//                if (note.getNotePhotoUri() != null) {
//                    setSelectedPhoto(note.getNotePhotoUri());
//                }

        mMainViewModel.getNoteCheckList(noteId).observe(this, new Observer<List<CheckItem>>() {
            @Override
            public void onChanged(List<CheckItem> checkItems) {
                mItems.clear();
                mItems.addAll(checkItems);
                mChecklistAdapter.notifyDataSetChanged();
            }
        });

        switch (note.getNoteColorId()) {
            case R.color.white:
                mRadioGroup.check(R.id.white_radio_button);
                //mCardView.setBackgroundColor(getResources().getColor(note.getNoteColorId()));
                break;
            case R.color.blue:
                mRadioGroup.check(R.id.blue_radio_button);
                //mCardView.setBackgroundColor(getResources().getColor(note.getNoteColorId()));
                break;
            case R.color.red:
                mRadioGroup.check(R.id.red_radio_button);
                //mCardView.setBackgroundColor(getResources().getColor(note.getNoteColorId()));
                break;
            case R.color.yellow:
                mRadioGroup.check(R.id.yellow_radio_button);
                //mCardView.setBackgroundColor(getResources().getColor(note.getNoteColorId()));
                break;
        }
        mBackgroundColorId = note.getNoteColorId();
//            }
//        });

        //setNoteDataBinding();
        mBinding.setNote(note);
    }

    /**
     * Shows all note's data (text, photo, or check list) to be edited.
     *
     * @param noteText carry edited note's text.
     * @param photoUri carry edited note's photo uri.
     */
    private void editedNoteItems(String noteText, Uri photoUri, ArrayList<CheckItem> checkItemItems, int backgroundColorId) {
        mEditedNoteET.setText(noteText);

        if (photoUri != null) {
            setSelectedPhoto(photoUri);
        }

//        if (checkItemItems != null) {
//            if (checkItemItems.size() > 0) {
//                setCheckListItems(checkItemItems);
//            }
//        }

        switch (backgroundColorId) {
            case R.color.white:
                mRadioGroup.check(R.id.white_radio_button);
                mCardView.setBackgroundColor(getResources().getColor(backgroundColorId));
                break;
            case R.color.blue:
                mRadioGroup.check(R.id.blue_radio_button);
                mCardView.setBackgroundColor(getResources().getColor(backgroundColorId));
                break;
            case R.color.red:
                mRadioGroup.check(R.id.red_radio_button);
                mCardView.setBackgroundColor(getResources().getColor(backgroundColorId));
                break;
            case R.color.yellow:
                mRadioGroup.check(R.id.yellow_radio_button);
                mCardView.setBackgroundColor(getResources().getColor(backgroundColorId));
                break;
        }
        mBackgroundColorId = backgroundColorId;
    }

    /**
     * This method will be called to show the added photo.
     *
     * @param photoUri is the Uri of selected photo.
     */
    private void setSelectedPhoto(Uri photoUri) {
        //mEditedPhotoIV.setImageURI(photoUri);
        //mEditedPhotoIV.setVisibility(View.VISIBLE);
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
                //mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.blue_radio_button:
                mBackgroundColorId = R.color.blue;
                //mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.red_radio_button:
                mBackgroundColorId = R.color.red;
                //mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.yellow_radio_button:
                mBackgroundColorId = R.color.yellow;
                //mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
        }
    }

//    /**
//     * Start a new activity to display image in a full size.
//     */
//    public void viewPhoto(Uri uri) {
//        Intent intent = new Intent(EditNoteActivity.this, ViewPhotoActivity.class);
//        intent.putExtra(Constants.EXTRA_PHOTO_VIEW_URI, uri);
//        startActivityForResult(intent, VIEW_PHOTO);
//    }

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
//        mEditedPhotoIV.setImageURI(null);
//        mEditedPhotoIV.setVisibility(View.GONE);
        mSelectedPhotoUri = null;
        setNoteDataBinding();
    }

    /**
     * Setting edited note's checkList items.
     */
//    private void setCheckListItems(ArrayList<CheckItem> checkItem) {
    private void setCheckListItems(int noteId) {
        //mLinearLayout.setVisibility(View.VISIBLE);

//        for (int i = 0; i < checkItem.size(); i++) {
//            CheckItem checkItemItems = checkItem.get(i);
//            String checkBoxItemText = checkItemItems.getCheckBoxItemText();
//            boolean checkBoxItemStatus = checkItemItems.getCheckBoxItemStatus();
//
//            addToArrayList(checkBoxItemText, checkBoxItemStatus);
//        }

        addToArrayList(noteId);
    }

    /**
     * Adding checkList items to Database.
     */
    public void addNewChecklistItem(long position) {
        String checklistItemText = mAddNewChecklistItemET.getText().toString();
//        boolean checkListItemStatus = false;
//
//        addToArrayList(checklistItemText, checkListItemStatus);

        checkItem.setNoteId(position);
        checkItem.setCheckBoxItemText(checklistItemText);
        checkItem.setCheckBoxItemStatus(false);
        mMainViewModel.addCheckItem(checkItem);

        mAddNewChecklistItemET.setText("");

        addToArrayList(position);
    }

    /**
     * After adding new Check item, this method will gets all check list items to  display them.
     *
     * @param noteId is the ForeignKey in 'CheckItem' POJO file, to get items of that note.
     */
    private void addToArrayList(long noteId) {

    }

//    private void addToArrayList(String checklistItemText, boolean checkListItemStatus) {
//        CheckItem checkItem = new CheckItem(checklistItemText, checkListItemStatus);
//        mItems.add(checkItem);
//        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
//    }

    /**
     * Responding to ClickListener on checkList items and update the status of checkBox item.
     *
     * @param position       of clicked item.
     * @param checkBoxStatus isChecked value of checkBox.
     */
    private void onCheckListItemClicked(int position, boolean checkBoxStatus, long noteId) {
        CheckItem checkItem = mItems.get(position);
        checkItem.setCheckBoxItemStatus(checkBoxStatus);
        System.out.println("check list item: " + checkItem.getId() + " " + checkItem.getCheckBoxItemStatus());
        mMainViewModel.updateCheckItemStatus(checkItem);

//        mMainViewModel.getNoteCheckList(noteId).observe(this, new Observer<List<CheckItem>>() {
//            @Override
//            public void onChanged(List<CheckItem> checkItems) {
//                mChecklistAdapter.updateData(checkItems);
//                mItems = checkItems;
//            }
//        });

//        mItems.set(position, checkItem);
//        //mChecklistAdapter.notifyDataSetChanged();
//        mChecklistAdapter.notifyItemChanged(position);
    }

    public void submit(long noteId) {
        String editedNoteText = mEditedNoteET.getText().toString();

        if (editedNoteText.isEmpty() && mSelectedPhotoUri == null && mChecklistAdapter.getItemCount() == 0) {
            mMainViewModel.deleteNote(noteId);
            finish();
        } else {
            if (mItems.size() == 0) {
                mItems = null;
            }
        }

//        Intent intent = new Intent();
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_TEXT, editedNoteText);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI, mSelectedPhotoUri);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST, mItems);
//        intent.putExtra(Constants.EXTRA_NOTE_EDITING_COLOR, mBackgroundColorId);
//        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
//        setResult(RESULT_OK, intent);
//        finish();

        note.setId(noteId);
//        note.setNoteText(editedNoteText);
//        note.setNotePhotoUri(mSelectedPhotoUri);
//        note.setCheckItem(mItems);
//        note.setNoteColorId(mBackgroundColorId);
        setNoteDataBinding();
        mMainViewModel.updateNote(note);

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);
        setResult(RESULT_OK, intent);

        finish();
    }
}
