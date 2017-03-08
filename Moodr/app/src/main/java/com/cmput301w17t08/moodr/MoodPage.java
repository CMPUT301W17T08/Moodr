package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by Sal on 2017-03-06.
 */

public abstract class MoodPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
    }

    public Mood grabVariables(String owner) {

        Spinner emotion = (Spinner) findViewById(R.id.sp_emotion);

        //Mood mood = new Mood(owner, emotion);

        EditText socialSituation = (EditText) findViewById(R.id.et_social_situation);
        EditText trigger = (EditText) findViewById(R.id.et_trigger);

        button = (Button) findViewById(R.id.btn_location);
        imageView = (ImageView) findViewById(R.id.iv_imageview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return mood;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
