package com.barmej.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ViewPhotoActivity extends AppCompatActivity {
    ImageView mViewedPhotoIV;
    Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        mViewedPhotoIV = findViewById(R.id.viewed_photo_image_view);

        Intent intent = getIntent();
        mPhotoUri = intent.getParcelableExtra(Constants.EXTRA_PHOTO_VIEW_URI);

        setSelectedPhoto(mPhotoUri);

        findViewById(R.id.delete_photo_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto();
            }
        });
    }

    private void setSelectedPhoto(Uri photoUri) {
        Log.i("photoUri", "photoUri Uri is: " + photoUri);
        mViewedPhotoIV.setImageURI(photoUri);
    }

    private void deletePhoto() {
        mPhotoUri = null;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
