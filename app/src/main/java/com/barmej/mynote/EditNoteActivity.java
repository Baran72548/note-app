package com.barmej.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.barmej.mynote.data.Note;

public class EditNoteActivity extends AppCompatActivity {
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditText = findViewById(R.id.edit_note_edit_text);
        Intent intent = getIntent();
        Note note = intent.getParcelableExtra(Constants.EXTRA_NOTE_EDITING);
        final int position = intent.getIntExtra(Constants.EXTRA_NOTE_POSITION, 0);
        //final Note position = intent.getParcelableExtra(Constants.EXTRA_NOTE_POSITION);
        //Note data = new Note(position);

        String text = note.getNoteText();
        mEditText.setText(text);

        findViewById(R.id.edit_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(position);
            }
        });
    }

    private void submit(int position) {

        Note note = new Note(mEditText.getText().toString());
        Toast.makeText(this, "wooow" + note, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_NOTE_EDITING, note);
        intent.putExtra(Constants.EXTRA_NOTE_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
