package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sal on 2017-03-06.
 */

public abstract class MoodPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Button button;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        addItemsOnSpinner();
    }

    public void addItemsOnSpinner(){


        spinner = (Spinner) findViewById(R.id.sp_emotion);
        List<String> categories = new ArrayList<String>();
        // Add the strings to the drop-down
        categories.add("Happy");
        categories.add("Sad");
        categories.add("Angry");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.
        simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // what goes here???
        Emotion emotion = new Emotion(spinner.getSelectedItem(), ?, ?);

    }




    public Mood grabVariables(String owner) {

        Mood mood = new Mood(owner, addItemsOnSpinner(). );

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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
