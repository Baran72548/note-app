package com.barmej.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.barmej.mynote.adapter.ChecklistAdapter;
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.listener.ItemClickListener;

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
        final ArrayList<CheckList> checkList = intent.getParcelableArrayListExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST);
        final int backgroundColorId = intent.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
        //Log.i("photoUri", "photoUri Uri is: " + photoUri);
        final int position = intent.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);

        editedNoteItems(note, photoUri, checkList, backgroundColorId);

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
                new ItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        onCheckListItemClicked(position);
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
    private void editedNoteItems(Note note, Uri photoUri, ArrayList<CheckList> checkList, int backgroundColorId) {
        String text = note.getNoteText();
        mEditedNoteET.setText(text);

        if (photoUri != null) {
            setSelectedPhoto(photoUri);
        }

        checkList = note.getNoteCheckList();
        if (checkList != null) {
            setCheckListItems(checkList);
            Log.i("photoUri", "checkBoxItemText is: not null");
        } else {
            Log.i("photoUri", "checkBoxItemText is: null");
        }


        //mRadioGroup.check(backgroundColorId);
        //setCardViewColor(backgroundColorId);
        mCardView.setBackgroundColor(backgroundColorId);
        switch (backgroundColorId) {
            case Color.WHITE:
                mRadioGroup.check(R.id.white_radio_button);
                mCardView.setBackgroundColor(backgroundColorId);
                break;
            case R.color.blue:
                mRadioGroup.check(R.id.blue_radio_button);
                mCardView.setBackgroundColor(backgroundColorId);
                break;
            case R.color.red:
                mRadioGroup.check(R.id.red_radio_button);
                mCardView.setBackgroundColor(backgroundColorId);
                break;
            case R.color.yellow:
                mRadioGroup.check(R.id.yellow_radio_button);
                break;
        }
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
                    mBackgroundColorId = Color.WHITE;
                    mCardView.setBackgroundColor(mBackgroundColorId);
                    break;
                case R.id.blue_radio_button:
                    mBackgroundColorId = getResources().getColor(R.color.blue);
                    mCardView.setBackgroundColor(mBackgroundColorId);
                    break;
                case R.id.red_radio_button:
                    mBackgroundColorId = getResources().getColor(R.color.red);
                    mCardView.setBackgroundColor(mBackgroundColorId);
                    break;
                case R.id.yellow_radio_button:
                    mBackgroundColorId = getResources().getColor(R.color.yellow);
                    mCardView.setBackgroundColor(mBackgroundColorId);
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

    private void setCheckListItems(ArrayList<CheckList> checkList) {
        mLinearLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < checkList.size(); i++) {
            CheckList checkListItems = checkList.get(i);
            String checkBoxItemText = checkListItems.getCheckListItemText();
            Log.i("photoUri", "checkBoxItemText is: " + checkBoxItemText);
            boolean checkBoxItemStatus = checkListItems.isCheckListItemStatus();

            CheckList checkListItems2 = new CheckList(checkBoxItemText, checkBoxItemStatus);
            mItems.add(checkListItems2);
            mChecklistAdapter.notifyItemInserted(mItems.size() -1);
        }
    }

    private void addNewChecklistItem() {
        String checklistItem = mAddNewChecklistItemET.getText().toString();
        CheckList checkList = new CheckList(checklistItem, false);
        mItems.add(checkList);
        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
        mAddNewChecklistItemET.setText("");
    }

    private void onCheckListItemClicked(int position) {
        CheckList checkList = mItems.get(position);
        String checkListItemName = checkList.getCheckListItemText();
        boolean checkListItemStatus = checkList.isCheckListItemStatus();

        if (!checkListItemStatus) {
            checkListItemStatus = true;

        } else {
            checkListItemStatus = false;
        }

        checkList = new CheckList(checkListItemName, checkListItemStatus);
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
