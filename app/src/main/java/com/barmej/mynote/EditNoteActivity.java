package com.barmej.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;

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
    private ArrayList<CheckItem> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        String noteText = intent.getStringExtra(Constants.EXTRA_NOTE_EDITING_TEXT);
        final Uri photoUri = intent.getParcelableExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI);
        ArrayList<CheckItem> checkItemItems = (ArrayList<CheckItem>) intent.getSerializableExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST);
        final int backgroundColorId = intent.getIntExtra(Constants.EXTRA_NOTE_EDITING_COLOR, 0);
        final int position = intent.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);

        mEditedNoteET = findViewById(R.id.edit_note_edit_text);
        mEditedPhotoIV = findViewById(R.id.edit_note_image_view);

        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);

        mLinearLayout = findViewById(R.id.checklist_layout);

        findViewById(R.id.edit_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(position);
            }
        });

        mEditedPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        //In case of adding a new list item, we need to make a recyclerView to display items in it.
        mRecyclerView = findViewById(R.id.checklist_recycler_view);
        mItems = CheckItem.getChecklistList();
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

        editedNoteItems(noteText, photoUri, checkItemItems, backgroundColorId);

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
     * @param noteText carry edited note's text.
     * @param photoUri carry edited note's photo uri.
     */
    private void editedNoteItems(String noteText, Uri photoUri, ArrayList<CheckItem> checkItemItems, int backgroundColorId) {
        mEditedNoteET.setText(noteText);

        if (photoUri != null) {
            setSelectedPhoto(photoUri);
        }

        if (checkItemItems != null) {
            if (checkItemItems.size() > 0) {
                setCheckListItems(checkItemItems);
            }
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

    /**
     * Setting selected color to CardView.
     */
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

    /**
     * Start a new activity to display image in a full size.
     */
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

    /**
     * This method will be called from onActivityResult (after returning from ViewPhotoActivity), if user deleted the image.
     */
    private void removePhoto() {
        mEditedPhotoIV.setImageURI(null);
        mEditedPhotoIV.setVisibility(View.GONE);
        mSelectedPhotoUri = null;
    }

    /**
     * Setting note's checkList items.
     */
    private void setCheckListItems(ArrayList<CheckItem> checkItem) {
        mLinearLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < checkItem.size(); i++) {
            CheckItem checkItemItems = checkItem.get(i);
            String checkBoxItemText = checkItemItems.getCheckBoxItemText();
            boolean checkBoxItemStatus = checkItemItems.getCheckBoxItemStatus();

            addToArrayList(checkBoxItemText, checkBoxItemStatus);
        }
    }

    /**
     * Adding checkList items to a recyclerView.
     */
    private void addNewChecklistItem() {
        String checklistItemText = mAddNewChecklistItemET.getText().toString();
        boolean checkListItemStatus = false;

        addToArrayList(checklistItemText, checkListItemStatus);
        mAddNewChecklistItemET.setText("");
    }

    private void addToArrayList(String checklistItemText, boolean checkListItemStatus) {
        CheckItem checkItem = new CheckItem(checklistItemText, checkListItemStatus);
        mItems.add(checkItem);
        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
    }

    /**
     * Responding to ClickListener on checkList items and update the status of checkBox item.
     * @param position of clicked item.
     * @param checkBoxStatus isChecked value of checkBox.
     */
    private void onCheckListItemClicked(int position, boolean checkBoxStatus) {
        CheckItem checkItem = mItems.get(position);

        checkItem.setCheckBoxItemStatus(checkBoxStatus);

        mItems.set(position, checkItem);
        mChecklistAdapter.notifyDataSetChanged();
    }

    private void submit(int position) {
        String editedNoteText = mEditedNoteET.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_TEXT, editedNoteText);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_PHOTO_URI, mSelectedPhotoUri);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_CHECKLIST, mItems);
        intent.putExtra(Constants.EXTRA_NOTE_EDITING_COLOR, mBackgroundColorId);
        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
