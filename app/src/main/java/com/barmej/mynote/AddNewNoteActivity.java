package com.barmej.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import com.barmej.mynote.adapter.NoteAdapter;
import com.barmej.mynote.data.CheckItem;
import com.barmej.mynote.data.DataRepository;
import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.viewmodel.MainViewModel;
import com.barmej.mynote.databinding.ActivityAddNewNoteBinding;
import com.barmej.mynote.listener.CheckBoxClickListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEditText;
    ImageView mNewAddedPhotoIV;
    Uri mSelectedPhotoUri = null;
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
    //private ArrayList<CheckItem> mItems;
    private List<CheckItem> mItems;

    RecyclerView.LayoutManager mListLayoutManager;

    private MainViewModel mMainViewModel;

    private int noteId;
    private Note note;
    private CheckItem checkItem;

    private NoteAdapter mNoteAdapter;

    private ActivityAddNewNoteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_new_note);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_note);
        mBinding.setListener(this);

        mEditText = findViewById(R.id.add_note_edit_text);
        mNewAddedPhotoIV = findViewById(R.id.new_added_photo_image_view);
        mRadioGroup = findViewById(R.id.colors_radio_group);
        mCardView = findViewById(R.id.activity_card_view_background_color);
        mLinearLayout = findViewById(R.id.checklist_layout);

        note = new Note();
        checkItem = new CheckItem();
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mNoteAdapter = new NoteAdapter();

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
                setNoteDataBinding();
            }
        });

        mNewAddedPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPhoto();
            }
        });

//        findViewById(R.id.add_new_note_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                submit();
//            }
//        });

        //In case of adding a new list item, we need to make a recyclerView to display items in it.
        mRecyclerView = findViewById(R.id.checklist_recycler_view);
        //mItems = CheckItem.getChecklistList();
        mItems = new ArrayList<>();
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

        addNote();
        mBinding.setNote(note);
    }

    /**
     * Set note's data then add them to database.
     */
    private void addNote() {
        setNoteDataBinding();
        noteId = (int)mMainViewModel.addNoteInfo(note);
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
        setNoteDataBinding();
        //mNewAddedPhotoIV.setVisibility(View.VISIBLE);
        //mNewAddedPhotoIV.setImageURI(data);
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
        //mNewAddedPhotoIV.setVisibility(View.GONE);
        mSelectedPhotoUri = null;
        setNoteDataBinding();
    }

    /**
     * Setting selected color to CardView.
     */
    private void setCardViewColor() {
        checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();

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

    /**
     * Adding checkList items to Database.
     */
    private void addNewChecklistItem() {
        String checklistItemText = mAddNewChecklistItemET.getText().toString();

        checkItem.setNoteId(noteId);
        checkItem.setCheckBoxItemText(checklistItemText);
        checkItem.setCheckBoxItemStatus(false);
        mMainViewModel.addCheckItem(checkItem);

//        mMainViewModel.getCheckItems().observe(this, new Observer<List<CheckItem>>() {
//            @Override
//            public void onChanged(List<CheckItem> checkItems) {
//                mChecklistAdapter.updateData(checkItems);
//            }
//        });

//        CheckItem checkItem = new CheckItem(checklistItemText, false);
//
//        mItems.add(checkItem);
//        mChecklistAdapter.notifyItemInserted(mItems.size() -1);
        mAddNewChecklistItemET.setText("");
        addToArrayList(noteId);
    }

    /**
     * After adding new Check item, this method will get all check list items to  display them.
     * @param noteId is the ForeignKey in 'CheckItem' POJO file, to get items of that note.
     */
    private void addToArrayList(int noteId) {
        mMainViewModel.getNoteCheckList(noteId).observe(this, new Observer<List<CheckItem>>() {
            @Override
            public void onChanged(List<CheckItem> checkItems) {
                //mChecklistAdapter.updateData(checkItems);
                mItems = checkItems;
                mChecklistAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Responding to ClickListener on checkList items and update the status of checkBox item.
     * @param position of clicked item.
     * @param checkBoxStatus isChecked value of checkBox.
     */
    private void onCheckListItemClicked(int position, boolean checkBoxStatus) {
        CheckItem checkItem = mItems.get(position);

        checkItem.setCheckBoxItemStatus(checkBoxStatus);

        mMainViewModel.updateCheckItemStatus(checkItem);

//        mItems.set(position, checkItem);
//        mChecklistAdapter.notifyDataSetChanged();

    }

    private void submit() {
        String noteText = mEditText.getText().toString();

        if (noteText.isEmpty() && mSelectedPhotoUri == null && mChecklistAdapter.getItemCount() == 0) {
            mMainViewModel.deleteNote(noteId);

//            Log.i("delete note", "note deleted" + note);
//            Intent intent = new Intent();
//            intent.putExtra("s", 0);
//            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (mChecklistAdapter.getItemCount() == 0) {
                mItems = null;
            }

            note.setId(noteId);
            setNoteDataBinding();
//            note.setNoteText(noteText);
//            note.setNotePhotoUri(mSelectedPhotoUri);
//            note.setCheckItem(mItems);
//            note.setNoteColorId(mBackgroundColorId);
            mMainViewModel.updateNote(note);
            Log.i("update note", "note updated");
            //mMainViewModel.addNoteInfo(note);

            //Intent intent = new Intent();
//            intent.putExtra(Constants.EXTRA_NOTE_TEXT, noteText);
//            intent.putExtra(Constants.EXTRA_NOTE_PHOTO_URI, mSelectedPhotoUri);
//            intent.putExtra(Constants.EXTRA_NOTE_CHECKLIST, mItems);
//            intent.putExtra(Constants.EXTRA_NOTE_COLOR_NAME, mBackgroundColorId);
//            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Update note's info in database.
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
            Log.i("update note", "note updated");

            finish();
        }
    }
}
