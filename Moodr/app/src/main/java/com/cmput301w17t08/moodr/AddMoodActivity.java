package com.cmput301w17t08.moodr;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * AddMoodActivity Class creates a new mood which the user can set the inputs to what they desire
 * with some limitations.
 * Some features do not work yet, this includes: Location reverse geocoded to actual addresses,
 * image encoding, and character limits. Each mood is seperately added onto the elasticsearch
 * server
 */


public class AddMoodActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_OPEN_CAMERA = 2;
    private static final int byteLimit = 65536;

    private static final String TAG = "MainActivity";

    private Coordinate coordinate = null;
    private ImageView imageView;
    private Button locationButton;
    private Button btnChoosePhoto;
    private Button btnOpenCamera;
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
    private String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

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
        situation_categories.add("");
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

                switch (selected_emotion) {
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
        imageView = (ImageView) findViewById(R.id.iv_imageview);

        // Open camera on button click and use for the picture
        btnOpenCamera = (Button) findViewById(R.id.btn_camera);
        btnOpenCamera.setOnClickListener(btnOpenCameraPressed);


        // Get image file on button click
        btnChoosePhoto = (Button) findViewById(R.id.btn_picture);
        //btnChoosePhoto.setOnClickListener(btnChoosePhotoPressed);

        // Get user location on button click
        locationButton = (Button) findViewById(R.id.btn_location);
        locationText = (TextView) findViewById(R.id.tv_location);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationText.setText(location.getLatitude() + " " + location.getLongitude());
                if (coordinate != null) {
                    coordinate.setLat(location.getLatitude());
                    coordinate.setLon(location.getLongitude());
                } else {
                    coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                }

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
                finish();
                return true;

            // Checkmark button
            case R.id.action_add_complete:
                // Create mood and send it right to elasticSearch
                createMood(emotion, situation, trigger, encodedImage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createMood(Emotion emotion, String situation, String trigger, String encodedImage) {
        // Grab owner
        owner = CurrentUserSingleton.getInstance().getUser().getName();
        // Create the mood
        Mood mood = new Mood(owner, emotion);

        mood.setSituation(situation);

        if (coordinate != null) {
            mood.setLocation(coordinate);
        }

        trigger = editTrigger.getText().toString();
        boolean checkLimit = countLimit();
        mood.setTrigger(trigger);

        mood.setImgUrl(encodedImage);

        // Check if limit is reached
        if (checkLimit) {
            // Check if app is connected to a network.
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null == activeNetwork) {
                // Generate a unique UUID ID for offline mode.
                mood.setId(UUID.randomUUID().toString());
                CurrentUserSingleton.getInstance().getMyMoodList().add(mood);
                Toast.makeText(getApplicationContext(), "This mood will be saved in database once Moodr has internet connection.", Toast.LENGTH_SHORT).show();
                CurrentUserSingleton.getInstance().getMyOfflineActions().addAction(1, mood);
                new SaveSingleton(getApplicationContext()).SaveSingletons(); // save singleton to disk.
                finish();
            } else {
                ElasticSearchMoodController.AddMoodTask addMoodTask = new ElasticSearchMoodController.AddMoodTask();
                addMoodTask.execute(mood);
                try {
                    String moodId = addMoodTask.get();
                    mood.setId(moodId);
                    CurrentUserSingleton.getInstance().getMyMoodList().add(mood);
                    new SaveSingleton(getApplicationContext()).SaveSingletons(); // save singleton to disk.
                } catch (Exception e) {
                    Log.i("Error", "Error getting moods out of async object");
                }
                finish();
            }
        }
        else {
            triggerError();
        }

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


    /* ------------------- Functions for image addition ------------------ */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */


    public View.OnClickListener btnOpenCameraPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openCamera();
        }
    };

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RESULT_OPEN_CAMERA);
        }
    }


    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            displayAndSetImage(picturePath);

        }
        if (requestCode == RESULT_OPEN_CAMERA && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            encodedImage = encodeImage(photo);
            Double imageSize = 4*Math.ceil((encodedImage.length()/3));
            if(imageSize > byteLimit){
                Toast.makeText(AddMoodActivity.this, "File size is too large",Toast.LENGTH_SHORT).show();
            }else{
                imageView.setImageBitmap(photo);
            }

            Toast.makeText(AddMoodActivity.this, "Image Added",Toast.LENGTH_SHORT).show();

        }
    }

    private void displayAndSetImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
            encodedImage = encodeImage(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    /* When button for image is pressed */
    public View.OnClickListener btnChoosePhotoPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseImage();
        }
    };


    public static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }





    /* Functions for character limit on trigger, 20 characters or 3 words */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */
    /* ------------------------------------------------------------------- */
    public boolean countLimit() {
        trigger = editTrigger.getText().toString();
        int triggerLength = trigger.length();
        int triggerWords = wordCount(trigger);
        boolean flag = true;
        if (triggerLength > 20 || triggerWords > 3) {
            flag = false;
        }
        return flag;

    }

    public int wordCount(String s) {
        String input = s.trim();
        int words = input.isEmpty() ? 0 : input.split("\\s+").length;
        return words;
    }

    public void triggerError() {
        new AlertDialog.Builder(AddMoodActivity.this)
                .setTitle("Limit Reached")
                .setMessage("Please use only 3 words or 20 characters")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}