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

    MainViewModel mMainViewModel;

    private ActivityViewPhotoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_photo);

        mBinding.setListener(this);

        mNote = new Note();

        mViewedPhotoIV = findViewById(R.id.viewed_photo_image_view);

        Intent intent = getIntent();
        mPhotoUri = intent.getParcelableExtra(Constants.EXTRA_PHOTO_VIEW_URI);

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mNote.setNotePhotoUri(mPhotoUri);

        mBinding.setNote(mNote);
    }

    /**
     * Delete uploaded image.
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
