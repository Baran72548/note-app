package com.barmej.mynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.barmej.mynote.data.Note;
import com.barmej.mynote.data.viewmodel.MainViewModel;
import com.barmej.mynote.databinding.ActivityViewPhotoBinding;

public class ViewPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView mViewedPhotoIV;
    Uri mPhotoUri;

    private Note mNote;
    private int noteId;

    MainViewModel mMainViewModel;

    private ActivityViewPhotoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_photo);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_photo);

        mBinding.setListener(this);

        mNote = new Note();

        mViewedPhotoIV = findViewById(R.id.viewed_photo_image_view);

        Intent intent = getIntent();
        mPhotoUri = intent.getParcelableExtra(Constants.EXTRA_PHOTO_VIEW_URI);
        //noteId = intent.getIntExtra(Constants.EXTRA_PHOTO_VIEW_URI, 0);

        //setSelectedPhoto(mPhotoUri);

//        findViewById(R.id.delete_photo_image_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deletePhoto();
//            }
//        });

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);;
        //mNote = mMainViewModel.getNoteInfo2(noteId);
        mNote.setNotePhotoUri(mPhotoUri);

        mBinding.setNote(mNote);
    }

    /**
     * View uploaded image in a full screen.
     */
    private void setSelectedPhoto(Uri photoUri) {
        //mViewedPhotoIV.setImageURI(photoUri);
    }

    /**
     * Delete uploaded image.
     */
    public void deletePhoto(View view) {
        //mPhotoUri = null;
        mNote.setId(noteId);
        mNote.setNotePhotoUri(null);
        mMainViewModel.updateNote(mNote);

//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        //mPhotoUri = null;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
