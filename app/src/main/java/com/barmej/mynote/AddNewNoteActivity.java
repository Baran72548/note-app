package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.barmej.mynote.adapter.ChecklistAdapter;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.viewmodel.MainViewModel;
import com.barmej.mynote.databinding.ActivityAddNewNoteBinding;
import com.barmej.mynote.listener.CheckBoxClickListener;
import com.barmej.mynote.listener.OnNoteAddListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText;
    private ImageView mNewAddedPhotoIV;
    private Uri mSelectedPhotoUri = null;
    private EditText mAddNewChecklistItemET;
    private LinearLayout mLinearLayout;

    private int mBackgroundColorId = R.color.white;
    private int checkedRadioButtonId;

    private RadioGroup mRadioGroup;

    private static final int VIEW_PHOTO = 110;
    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 120;
    private static final int PICK_IMAGE = 130;

    private RecyclerView mRecyclerView;
    private ChecklistAdapter mChecklistAdapter;
    private List<CheckItem> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    private MainViewModel mMainViewModel;

    private long noteId;
    private Note note;
    private CheckItem checkItem;

    private ActivityAddNewNoteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_note);
        mBinding.setListener(this);

        mEditText = findViewById(R.id.add_note_edit_text);
        mNewAddedPhotoIV = findViewById(R.id.new_added_photo_image_view);
        mRadioGroup = findViewById(R.id.colors_radio_group);
        mLinearLayout = findViewById(R.id.checklist_layout);

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

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

        //In case of adding a new list item, we need to make a recyclerView to display items in it.
        mRecyclerView = findViewById(R.id.checklist_recycler_view);
        mItems = new ArrayList<>();
        mChecklistAdapter = new ChecklistAdapter(new CheckBoxClickListener() {
            @Override
            public void onCheckBoxClickListener(CheckItem checkItem, boolean checked) {
                onCheckListItemClicked(checkItem, checked);
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

        note = new Note();
        mBinding.setNote(note);
        addNote();
    }

    /**
     * Set note's data then add them to database.
     */
    private void addNote() {
        setNoteDataBinding();
        mMainViewModel.addNoteInfo(note, new OnNoteAddListener() {
            @Override
            public void onNoteAdded(long id) {
                noteId = id;
            }
        });
    }

    /**
     * Setting note's data for binding.
     */
    private void setNoteDataBinding() {
        note.setNoteText(mEditText.getText().toString());
        note.setNotePhotoUri(mSelectedPhotoUri);
        note.setCheckItem(mItems);
        note.setNoteColorId(mBackgroundColorId);
        mBinding.setNote(note);
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

    public void selectPhoto() {
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
        mSelectedPhotoUri = data;
        note.setNotePhotoUri(mSelectedPhotoUri);
        mBinding.setNote(note);
    }

    /**
     * Start a new activity to display image in a full size.
     */
    private void viewPhoto() {
        Intent intent = new Intent(AddNewNoteActivity.this, ViewPhotoActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_VIEW_URI, mSelectedPhotoUri);
        startActivityForResult(intent, VIEW_PHOTO);
    }

    /**
     * This method will be called from onActivityResult (after returning from ViewPhotoActivity), if user deleted the image.
     */
    private void removePhoto() {
        mNewAddedPhotoIV.setImageURI(null);
        mSelectedPhotoUri = null;
        note.setNotePhotoUri(mSelectedPhotoUri);
        mBinding.setNote(note);
    }

    /**
     * Setting selected color to CardView.
     */
    private void setCardViewColor() {
        checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();

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

        note.setNoteColorId(mBackgroundColorId);
        mBinding.setNote(note);
    }

    /**
     * Adding checkList items to Database.
     */
    private void addNewChecklistItem() {
        checkItem = new CheckItem();
        checkItem.setNoteId(noteId);
        checkItem.setCheckBoxItemText(mAddNewChecklistItemET.getText().toString());
        checkItem.setCheckBoxItemStatus(false);
        mMainViewModel.addCheckItem(checkItem);

        mMainViewModel.getNoteCheckList(noteId).observe(this, new Observer<List<CheckItem>>() {
            @Override
            public void onChanged(List<CheckItem> checkItems) {
                mChecklistAdapter.submitList(checkItems);
                mItems.clear();
                mItems.addAll(checkItems);
            }
        });

        mAddNewChecklistItemET.setText("");
    }

    /**
     * Responding to ClickListener on checkList items and update the status of checkBox item.
     * @param checkItem is the clicked item.
     * @param checkBoxStatus isChecked value of checkBox.
     */
    private void onCheckListItemClicked(CheckItem checkItem, boolean checkBoxStatus) {
        checkItem.setCheckBoxItemStatus(checkBoxStatus);
        mMainViewModel.updateCheckItemStatus(checkItem);
    }

    /**
     * Click on "Done" button will update note's info in database.
     */
    @Override
    public void onClick(View view) {
        if (mEditText.getText().toString().isEmpty() && mSelectedPhotoUri == null && mChecklistAdapter.getItemCount() == 0) {
            mMainViewModel.deleteNote(noteId);
            finish();

        } else {
            if (mChecklistAdapter.getItemCount() == 0) {
                mItems = null;
            }

            note.setId(noteId);
            setNoteDataBinding();
            mMainViewModel.updateNote(note);

            finish();
        }
    }
}
