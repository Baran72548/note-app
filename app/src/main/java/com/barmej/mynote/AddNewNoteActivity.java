package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.barmej.mynote.adapter.ChecklistAdapter;
import com.barmej.mynote.data.CheckList;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;

public class AddNewNoteActivity extends AppCompatActivity {

    EditText mEditText;
    ImageView mNewAddedPhotoIV;
    Uri mSelectedPhotoUri;
    EditText mAddNewChecklistItemET;
    LinearLayout mLinearLayout;

    int mBackgroundColorId = R.color.white;
    int checkedRadioButtonId;

    private RadioGroup mRadioGroup;
    private CardView mCardView;

    private static final int VIEW_PHOTO = 110;
    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 120;
    private static final int PICK_IMAGE = 130;

    private RecyclerView mRecyclerView;
    private ChecklistAdapter mChecklistAdapter;
    private ArrayList<CheckList> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        mEditText = findViewById(R.id.add_note_edit_text);
        mNewAddedPhotoIV = findViewById(R.id.new_added_photo_image_view);
        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);
        mLinearLayout = findViewById(R.id.checklist_layout);

        findViewById(R.id.photo_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
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

        mNewAddedPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPhoto();
            }
        });

        findViewById(R.id.add_new_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
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

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedPhoto(data.getData());
                // This method help us to keep the permission to use the returned uri from getData().
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, R.string.failed_to_get_image, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == VIEW_PHOTO) {
            if (resultCode == RESULT_OK && data != null) {
                removePhoto();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhotoIntent();
            } else {
                Toast.makeText(this, R.string.read_permission_needed_to_access_files, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectPhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_PHOTO_FROM_GALLERY_PERMISSION);
        } else {
            pickPhotoIntent();
        }
    }

    private void pickPhotoIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    private void setSelectedPhoto(Uri data) {
        mNewAddedPhotoIV.setVisibility(View.VISIBLE);
        mNewAddedPhotoIV.setImageURI(data);
        mSelectedPhotoUri = data;
    }

    private void viewPhoto() {
        Intent intent = new Intent(AddNewNoteActivity.this, ViewPhotoActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_VIEW_URI, mSelectedPhotoUri);
        startActivityForResult(intent, VIEW_PHOTO);
    }

    private void removePhoto() {
        mNewAddedPhotoIV.setImageURI(null);
        mNewAddedPhotoIV.setVisibility(View.GONE);
        mSelectedPhotoUri = null;
    }

    private void setCardViewColor() {
        checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();

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

    private void addNewChecklistItem() {
        String checklistItemText = mAddNewChecklistItemET.getText().toString();
        //CheckBox checkBoxStatus = new CheckBox(this);
        boolean checkListItemStatus = false;

        CheckList checkList = new CheckList(checklistItemText, false);
        //Log.i("Array Content is: ", checklistItemText + checkListItemStatus);

        mItems.add(checkList);
        Log.i("array items: ", "Add add items: " + mItems);
        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
        mAddNewChecklistItemET.setText("");
    }

    private void onCheckListItemClicked(int position, boolean checkBoxStatus) {
        Toast.makeText(this, "clicked on item: " + checkBoxStatus, Toast.LENGTH_LONG).show();
        CheckList checkList = mItems.get(position);
        String checklistItemText = checkList.getCheckListItemText();
        //boolean checkListItemStatus = checkList.getCheckListItemStatus();

        if (checkBoxStatus) {
            checkList.setCheckListItemStatus(true);
        } else {
            checkList.setCheckListItemStatus(false);
        }

        Log.i("Array Content is: ", checklistItemText + checkBoxStatus);

//        checkList = new CheckList(checklistItemText, checkBoxStatus);
        mItems.set(position, checkList);
        mChecklistAdapter.notifyItemChanged(position);


//        if(checkListItemStatus) {
//            checkList.setCheckListItemStatus(checkListItemStatus);
//
//        } else {
//            checkListItemStatus = true;
//            Log.i("Array Content is: ", "ss" + checkListItemStatus);
//        }

//        checkBoxStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(checkBoxStatus.isChecked()) {
//                        Log.i("Array Content is: ", "true");
//                        checkBoxStatus.setPaintFlags(checkBoxStatus.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    } else {
//                        if ((checkBoxStatus.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
//                            Log.i("Array Content is: ", "false");
//                            checkBoxStatus.setPaintFlags( checkBoxStatus.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                        }
//                    }
//                }
//            });
//        checkListItemStatus = checkBoxStatus.isChecked();
//        Log.i("Array Content is: ", "status " + checkListItemStatus);


//        if (checkBoxStatus.isChecked()) {
//            checkListItemStatus = false;
//            Log.i("Array Content is: ", "true");
//        } else {
//            checkListItemStatus = true;
//            Log.i("Array Content is: ", "false");
//        }

//        checkList = new CheckList(checklistItemText, checkListItemStatus);
//        mItems.set(position, checkList);
//        mChecklistAdapter.notifyItemChanged(position);

//        CheckList checkList = mItems.get(position);
//        String checkListItemName = checkList.getCheckListItemText();
//        boolean checkListItemStatus = checkList.getCheckListItemStatus();
//
//        if (!checkListItemStatus) {
//            checkListItemStatus = true;
//
//        } else {
//            checkListItemStatus = false;
//        }
//
//        checkList = new CheckList(checkListItemName, checkListItemStatus);
//        mItems.set(position, checkList);
//        mChecklistAdapter.notifyItemChanged(position);
    }

    private void submit() {
        String noteText = mEditText.getText().toString();

//        ArrayList<String> listItems = new ArrayList<>();
//        for(int index = 0; index < mItems.size(); index++) {
//            String listItemText = mItems.get(index).toString();
//            Log.i("Array Content is: ", listItemText);
//            listItems.add(listItemText);
//        }

        if (noteText.isEmpty() && mSelectedPhotoUri == null && mItems.size() == 0) {
            finish();
        } else {
            Log.v("noteText", noteText);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_NOTE_TEXT, noteText);
            intent.putExtra(Constants.EXTRA_NOTE_PHOTO_URI, mSelectedPhotoUri);
            intent.putParcelableArrayListExtra(Constants.EXTRA_NOTE_CHECKLIST, mItems);
            intent.putExtra(Constants.EXTRA_NOTE_COLOR_NAME, mBackgroundColorId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
