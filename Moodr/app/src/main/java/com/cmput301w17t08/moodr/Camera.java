package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/*
*
* Camera activity can be accessed by AddMoodActivity or EditActivity.
* User can take a picture in here and send the picture AddMoodActivity or EditActivity.
 */



public class Camera extends AppCompatActivity {

    Button btnOpenCamera;
    ImageView imageView;
    int i;
    String encodedImage = "";
    Mood mood;
    int edit_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // from add activity
        if (getIntent().getSerializableExtra("add") != null) {
            mood = (Mood) getIntent().getSerializableExtra("add");
            i = 1;
            // from edit activity
        } else if (getIntent().getSerializableExtra("edit") != null) {
            mood = (Mood) getIntent().getSerializableExtra("edit");
            edit_index = getIntent().getIntExtra("edit_index", -1);
            i = 2;
        }

        imageView = (ImageView) findViewById(R.id.image);
        btnOpenCamera = (Button) findViewById(R.id.button);

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }


    public void back (View view) {
        if (i == 1) {
            Intent intent = new Intent(Camera.this, AddMoodActivity.class);
            intent.putExtra("addcam", mood);
            startActivity(intent);
        } else if (i == 2) {
            Intent intent = new Intent(Camera.this, EditMoodActivity.class);
            intent.putExtra("edit_index_cam", edit_index);
            intent.putExtra("editcam", mood);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        encodedImage = encodeImage(bitmap);
        mood.setImgUrl(encodedImage);
    }

    public static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}

