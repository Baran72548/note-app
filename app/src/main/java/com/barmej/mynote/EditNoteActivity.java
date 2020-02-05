package com.barmej.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.barmej.mynote.adapter.ChecklistAdapter;
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {
    EditText mEditedNoteET;
    ImageView mEditedPhotoIV;
    Uri mSelectedPhotoUri;
    EditText mAddNewChecklistItemET;

    int mBackgroundColorId;

    private RadioGroup mRadioGroup;
    private CardView mCardView;

    private static final int VIEW_PHOTO = 110;
    private static final int PICK_IMAGE = 130;

    LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private ChecklistAdapter mChecklistAdapter;
    private ArrayList<CheckList> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditedNoteET = findViewById(R.id.edit_note_edit_text);
        mEditedPhotoIV = findViewById(R.id.edit_note_image_view);

        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);

        mLinearLayout = findViewById(R.id.checklist_layout);

        Intent intent = getIntent();
        Note note = intent.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_TEXT);
        final Uri photoUri = intent.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI);
        final ArrayList<CheckList> checkListItems = intent.getParcelableArrayListExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST);
        final int backgroundColorId = intent.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
        final int position = intent.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);

        Log.i("array items: ", "Edit intent items: " + checkListItems);
        editedNoteItems(note, photoUri, checkListItems, backgroundColorId);

        mEditedPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPhoto();
            }
        });

        findViewById(R.id.edit_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(position);
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
            }
        });

        //In case of adding a new list item, we need to make a recyclerView to display items in it.
        mRecyclerView = findViewById(R.id.checklist_recycler_view);
        mItems = CheckList.getChecklistList();
        mChecklistAdapter = new ChecklistAdapter(mItems,
                new CheckBoxClickListener() {
                    @Override
                    public void onCheckBoxClickListener(int position, boolean checkBoxStatus) {
                        onCheckListItemClicked(position, checkBoxStatus);
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
                addNewChecklistItem();
            }
        });
    }

    private void pickPhotoIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    /**
     * Shows all note's data (text, photo, or check list) to be edited.
     * @param note carry edited note's text.
     * @param photoUri carry edited note's photo uri.
     */
    private void editedNoteItems(Note note, Uri photoUri, ArrayList<CheckList> checkListItems, int backgroundColorId) {
        String text = note.getNoteText();
        mEditedNoteET.setText(text);

        if (photoUri != null) {
            setSelectedPhoto(photoUri);
        }

        Log.i("array items: ", "Edit edit items: " + checkListItems);
        //checkListItems = note.getNoteCheckList();
        if (checkListItems != null) {
            Log.i("checkBoxArray", "checkBoxItemText is: not null");
            setCheckListItems(checkListItems);
        } else {
            Log.i("checkBoxArray", "checkBoxItemText is: null");
        }

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
     * This method will be called to show the edited or added photo.
     * @param photoUri is the Uri of selected photo.
     */
    private void setSelectedPhoto(Uri photoUri) {
        mEditedPhotoIV.setImageURI(photoUri);
        mEditedPhotoIV.setVisibility(View.VISIBLE);
        mSelectedPhotoUri = photoUri;
    }

    private void setCardViewColor() {
        int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.white_radio_button:
                mBackgroundColorId = R.color.white;
                mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.blue_radio_button:
                mBackgroundColorId = R.color.blue;
                mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.red_radio_button:
                mBackgroundColorId = R.color.red;
                mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
            case R.id.yellow_radio_button:
                mBackgroundColorId = R.color.yellow;
                mCardView.setBackgroundColor(getResources().getColor(mBackgroundColorId));
                break;
        }
    }

    private void viewPhoto() {
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

    private void removePhoto() {
        mEditedPhotoIV.setImageURI(null);
        mEditedPhotoIV.setVisibility(View.GONE);
        mSelectedPhotoUri = null;
    }

    /**
     *    2
     */
    private void setCheckListItems(ArrayList<CheckList> checkList) {
        Log.i("array items: ", "Edit set items: " + checkList);
        mLinearLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < 1; i++) {
            CheckList checkListItems = checkList.get(i);
            String checkBoxItemText = checkListItems.getCheckListItemText();
            Log.i("photoUri", "for checkBoxItemText is: " + checkBoxItemText);
            boolean checkBoxItemStatus = checkListItems.isCheckListItemStatus();
            //CheckBox checkBoxStatus = null;
            //checkBoxStatus.setChecked(false);
            Log.i("photoUri", "for checkBoxItemState is: " + checkBoxItemStatus);

            mEditedNoteET.setText(checkBoxItemText + checkBoxItemStatus);
            //addToArrayList(checkBoxItemText, checkBoxItemStatus);
        }
    }

    private void addToArrayList(String text, boolean status) {
        CheckList checkListItems = new CheckList(text, status);
        mItems.add(checkListItems);
        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
    }

    private void addNewChecklistItem() {
        String checklistItemText = mAddNewChecklistItemET.getText().toString();
//        CheckBox checkBoxStatus = null;
//        checkBoxStatus.setChecked(false);
//        CheckList checkList = new CheckList(checklistItem, checkBoxStatus);
        boolean checkListItemStatus = false;

        CheckList checkList = new CheckList(checklistItemText, checkListItemStatus);
        mItems.add(checkList);
        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
        mAddNewChecklistItemET.setText("");
    }

    private void onCheckListItemClicked(int position, boolean checkBoxStatus) {
        CheckList checkList = mItems.get(position);
        String checkListItemName = checkList.getCheckListItemText();
        boolean checkListItemStatus = checkList.isCheckListItemStatus();
        CheckBox checkBoxStatus2 = null;
        checkBoxStatus2.setChecked(false);

        if (!checkListItemStatus) {
            checkListItemStatus = true;

        } else {
            checkListItemStatus = false;
        }

        checkList = new CheckList(checkListItemName, checkBoxStatus);
        mItems.set(position, checkList);
        mChecklistAdapter.notifyItemChanged(position);
    }

    private void submit(int position) {
        String editedNoteText = mEditedNoteET.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_TEXT, editedNoteText);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI, mSelectedPhotoUri);
        intent.putParcelableArrayListExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST, mItems);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_COLOR, mBackgroundColorId);
        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
