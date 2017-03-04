package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LatestActivity extends AppCompatActivity {
    private LatestMoodListAdapter adapter;
    private ListView moods_listview;
    private ArrayList<Mood> latest_moods; // will update type later?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);

        moods_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to mood
            }
        });
    }

    private ArrayList<Mood> getLatest(){
        // need elastic search to implement
        return null;
    }

    public void onStart() {
        latest_moods = getLatest();

        adapter = new LatestMoodListAdapter(this, latest_moods);

        moods_listview = (ListView) findViewById(R.id.latest_list);
        moods_listview.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }




}
