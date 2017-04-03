package com.cmput301w17t08.moodr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 *
 * This activity displays the latest mood of all friends on the user's friends list. It finds
 * the latest moods using elastic search.
 *
 */
public class LatestActivity extends AppCompatActivity {
    private LatestMoodListAdapter adapter;
    private ListView moodsListview;
    private ArrayList<Mood> latestMoods; // will update type later?
    private User currentUser;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_latest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        currentUser = CurrentUserSingleton.getInstance().getUser();

        moodsListview = (ListView) findViewById(R.id.latest_list);

        moodsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToMood(latestMoods.get(position));
            }
        });

        FloatingActionButton map_buttom = (FloatingActionButton) findViewById(R.id.go_to_map);
        map_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Go to map activity", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LatestActivity.this, MapsLatestActivity.class);
                // Send filtered list to the map activity
                intent.putExtra("filteredList", latestMoods);
                startActivity(intent);
            }
        });

    }

    /**
     * Gets the latest moods of all friends of the user.
     * @return a list of moods
     */

    private ArrayList<Mood> getLatest(){
        ArrayList<String> friendsList= currentUser.getFriends();
        ArrayList<Mood> latest = new ArrayList<>();

        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            Toast.makeText(getApplicationContext(), "Unable to load moods when offline.", Toast.LENGTH_SHORT).show();
        } else {
            ElasticSearchMoodController.GetLatestMoodsTask getMoodTask = new ElasticSearchMoodController.GetLatestMoodsTask();
            getMoodTask.execute(friendsList);
            try {
                latest.addAll(getMoodTask.get());
            } catch (Exception e) {
                Log.i("Error", "Failed to get the moods out of the async object");
            }
        }

        return latest;
    }

    /**
     * Reloads the latest mood list.
     */
    private void refreshMoods(){
        latestMoods.clear();
        latestMoods.addAll(getLatest());
        adapter.notifyDataSetChanged();
    }

    /**
     * goes to the mood selected
     * @param mood the mood selected
     */
    private void goToMood(Mood mood){
        Intent intent = new Intent(this, ViewFriendMoodActivity.class);
        intent.putExtra("mood", mood);

        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        latestMoods = getLatest();

        adapter = new LatestMoodListAdapter(this, latestMoods);

        moodsListview.setAdapter(adapter);

        filter = adapter.getFilter();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.latest_menu, menu);
        getMenuInflater().inflate(R.menu.filter_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.filter_keyword);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter.filter("K:" + searchView.getQuery());
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter.filter("K:" + searchView.getQuery());
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.latest_refresh:
                refreshMoods();
                return true;

            case R.id.filter_all:
                filter.filter("");
                break;

            case R.id.filter_recent:
                filter.filter("LATEST");
                break;

            case R.id.filter_angry:
                filter.filter("E:" + Emotion.angry.getName());
                break;

            case R.id.filter_confused:
                filter.filter("E:" + Emotion.confused.getName());
                break;

            case R.id.filter_disgusted:
                filter.filter("E:" + Emotion.disgust.getName());
                break;

            case R.id.filter_happy:
                filter.filter("E:" + Emotion.happy.getName());
                break;

            case R.id.filter_sad:
                filter.filter("E:" + Emotion.sad.getName());
                break;

            case R.id.filter_scared:
                filter.filter("E:" + Emotion.fear.getName());
                break;

            case R.id.filter_shame:
                filter.filter("E:" + Emotion.shame.getName());
                break;

            case R.id.filter_surprise:
                filter.filter("E:" + Emotion.surprise.getName());
                break;
        }

        return super.onOptionsItemSelected(item);

    }

}
