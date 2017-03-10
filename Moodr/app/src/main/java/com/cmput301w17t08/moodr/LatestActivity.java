package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LatestActivity extends AppCompatActivity {
    private LatestMoodListAdapter adapter;
    private ListView moodsListview;
    private ArrayList<Mood> latestMoods; // will update type later?
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);
        currentUser = CurrentUserSingleton.getInstance().getUser();

        moodsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                goToMood(latestMoods.get(position));
            }
        });
    }

    private ArrayList<Mood> getLatest(){
        // need elastic search to implement
        ArrayList<String> friendsList= currentUser.getMyFriendList();

        // create a GetLatestMoodsTask later?
        ElasticSearchMoodController.GetMoodTask getMoodTask = new ElasticSearchMoodController.GetMoodTask();

        for (String friend : friendsList){
            getMoodTask.execute(friend);

            try {
                latestMoods.addAll(getMoodTask.get());
            }
            catch(Exception e){
                Log.i("Error", "Failed to get the moods out of the async object");
            }
        }

        return null;
    }

    private void refreshMoods(){
        latestMoods.clear();
        getLatest();
        adapter.notifyDataSetChanged();
    }

    private void goToMood(Mood mood){
        Intent intent = new Intent(this, ViewFriendMoodActivity.class);
        // pass mood into
        //startActivity(intent);
    }

    public void onStart() {
        latestMoods = getLatest();

        adapter = new LatestMoodListAdapter(this, latestMoods);

        moodsListview = (ListView) findViewById(R.id.latest_list);
        moodsListview.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
