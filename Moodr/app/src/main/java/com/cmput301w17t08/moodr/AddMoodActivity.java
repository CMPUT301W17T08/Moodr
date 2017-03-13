package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddMoodActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private ImageView imageView;
    private Button locationButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationText;

    // REMOVE WHEN TRANSFERRING
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        // Create the spinner drop-down
        Spinner emotion_spinner = (Spinner) findViewById(R.id.sp_emotion);
        List<String> emotion_categories = new ArrayList<String>();
        // Add the strings to the drop-down for mood
        emotion_categories.add("Happy");
        emotion_categories.add("Sad");
        emotion_categories.add("Angry");
        emotion_categories.add("Confused");
        emotion_categories.add("Disgust");
        emotion_categories.add("Scared");
        emotion_categories.add("Shame");
        emotion_categories.add("Surprised");


        // Create the spinner drop-down
        Spinner situation_spinner = (Spinner) findViewById(R.id.et_social_situation);
        List<String> situation_categories = new ArrayList<String>();
        // Strings for situations
        situation_categories.add("Alone");
        situation_categories.add("1 other person");
        situation_categories.add("2 to several people");
        situation_categories.add("Crowd");

        ArrayAdapter<String> emotionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, emotion_categories);

        ArrayAdapter<String> situationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, situation_categories);


        emotionAdapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);

        situationAdapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);

        emotion_spinner.setAdapter(emotionAdapter);
        situation_spinner.setAdapter(situationAdapter);


        EditText trigger = (EditText) findViewById(R.id.et_trigger);
        // If trigger is not set
        if (trigger.getText().equals("")) {
            trigger.setError("Please enter a trigger!");

        }

        Button btn_choose_photo = (Button) findViewById(R.id.btn_picture);
        imageView = (ImageView) findViewById(R.id.iv_imageview);
        btn_choose_photo.setOnClickListener(btnChoosePhotoPressed);

        // Get user location on button click
        locationButton = (Button) findViewById(R.id.btn_location);
        locationText = (TextView) findViewById(R.id.tv_location);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationText.append("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            acquireLocation();
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Adds the icons to the action bar is it present
        getMenuInflater().inflate(R.menu.menu_add_mood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_cancel:
                finish();
                return true;

            case R.id.action_add_complete:
                // Add the mood to MoodList
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                acquireLocation();
                break;
            default:
                break;
        }
    }

    public void acquireLocation() {
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddMoodActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddMoodActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });
    }


    public View.OnClickListener btnChoosePhotoPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseImage();
        }
    };

    /* Choose an image from Gallery */
    void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }
}