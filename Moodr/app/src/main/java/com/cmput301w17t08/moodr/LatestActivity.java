package com.cmput301w17t08.moodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LatestActivity extends AppCompatActivity {
    private ArrayAdapter<Mood> adapter;
    private ListView moods_listview;
    private ArrayList<Mood> latest_moods; // will update type later?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);

        moods_listview = (ListView) findViewById(R.id.latest_list);


    }

    private ArrayList<Mood> getLatest(){
        // need elastic search to implement
        return null;
    }

    public void onStart() {
        // fill later.
    }


}
