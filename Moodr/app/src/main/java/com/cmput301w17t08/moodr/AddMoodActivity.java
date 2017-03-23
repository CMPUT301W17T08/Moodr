package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AddMoodActivity Class creates a new mood which the user can set the inputs to what they desire
 * with some limitations.
 * Some features do not work yet, this includes: Location reverse geocoded to actual addresses,
 * image encoding, and character limits. Each mood is seperately added onto the elasticsearch
 * server
 */


public class AddMoodActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    private ImageView imageView;
    private ImageButton locationButton;
    private ImageButton btnChoosePhoto;
    private ImageButton btnOpenCamera;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationText;
    private ArrayAdapter<String> emotionAdapter;
    private ArrayAdapter<String> situationAdapter;
    private EditText editTrigger;
    private InputFilter filter;
    private String selected_emotion;
    private Date date;
    private String owner;
    private int id;
    private Emotion emotion;
    private String imgUrl;
    private String trigger;
    private String situation;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        // Create the spinner drop-down
        Spinner emotion_spinner = (Spinner) findViewById(R.id.sp_emotion);
        final List<String> emotion_categories = new ArrayList<String>();
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

        emotionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, emotion_categories);

        situationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, situation_categories);


        emotionAdapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);

        situationAdapter.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);

        emotion_spinner.setAdapter(emotionAdapter);
        situation_spinner.setAdapter(situationAdapter);


        emotion_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_emotion = parent.getItemAtPosition(position).toString();

                switch(selected_emotion){
                    case "Happy":
                        emotion = Emotion.happy;
                        break;
                    case "Sad":
                        emotion = Emotion.sad;
                        break;
                    case "Angry":
                        emotion = Emotion.angry;
                        break;
                    case "Confused":
                        emotion = Emotion.confused;
                        break;
                    case "Disgust":
                        emotion = Emotion.disgust;
                        break;
                    case "Scared":
                        emotion = Emotion.fear;
                        break;
                    case "Shame":
                        emotion = Emotion.shame;
                        break;
                    case "Surprised":
                        emotion = Emotion.surprise;
                        break;
                }
            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        situation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                situation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editTrigger = (EditText) findViewById(R.id.et_trigger);

        editTrigger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int wordsLength = countWords(s.toString());// words.length;
                // count == 0 means a new word is going to start
                if (count == 0 && wordsLength >= 3) {
                    setCharLimit(editTrigger, editTrigger.getText().length());
                } else {
                    removeFilter(editTrigger);
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        trigger = editTrigger.getText().toString();

        // Open camera on button click and use for the picture
        btnOpenCamera = (ImageButton) findViewById(R.id.btn_camera);
        imageView = (ImageView) findViewById(R.id.iv_imageview);
        btnOpenCamera.setOnClickListener(btnOpenCameraPressed);



        // Get image file on button click
        btnChoosePhoto = (ImageButton) findViewById(R.id.btn_picture);
        imageView = (ImageView) findViewById(R.id.iv_imageview);
        btnChoosePhoto.setOnClickListener(btnChoosePhotoPressed);

        // Get user location on button click
        locationButton = (ImageButton) findViewById(R.id.btn_location);
        locationText = (TextView) findViewById(R.id.tv_location);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationText.setText(location.getLongitude() + " " + location.getLatitude());
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

    // Creates the actionbar at the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds the icons to the action bar is it present
        getMenuInflater().inflate(R.menu.menu_add_mood, menu);
        return true;
    }

    // When one of the buttons are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // X button
            case R.id.action_add_cancel:
//                finish();
                Intent intent_cancel  = new Intent(this, MyProfileActivity.class);
                startActivity(intent_cancel);
                return true;

            // Checkmark button
            case R.id.action_add_complete:
                // Create mood and send it right to elasticSearch
                createMood(emotion, situation, trigger);

                setResult(RESULT_OK);

                // Add the mood to MoodList
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    public String encodeIMG(Uri uri){
        InputStream inputStream = new FileInputStream(uri);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    */

    public void createMood(Emotion emotion, String situation, String trigger) {
        // Grab owner
        owner = CurrentUserSingleton.getInstance().getUser().getName();
        // Create the mood
        Mood mood = new Mood(owner, emotion);

        id = CurrentUserSingleton.getInstance().getUser().getPostID();
        mood.setId(id);

        CurrentUserSingleton.getInstance().getUser().incrementPostID();

        location = locationText.getText().toString();

        mood.setSituation(situation);

        mood.setLocation(location);

        mood.setTrigger(trigger);

//        mood.setImgUrl("PLACEHOLDER");

        ElasticSearchMoodController.AddMoodTask addMoodTask = new ElasticSearchMoodController.AddMoodTask();
        CurrentUserSingleton.getInstance().getMyMoodList().add(mood);
        addMoodTask.execute(mood);

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

    /* Gets new location and makes changes to the old one if need be */
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

    /* Choose an image from Gallery */
    /* When button for image is pressed */
    public View.OnClickListener btnChoosePhotoPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseImage();
        }
    };

    public void chooseImage() {
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


    /* When button for camera is pressed */
    public View.OnClickListener btnOpenCameraPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openCamera();
        }
    };

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /* Functions for character limit on trigger, 20 characters or 3 words */
    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[]{filter});
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }


}