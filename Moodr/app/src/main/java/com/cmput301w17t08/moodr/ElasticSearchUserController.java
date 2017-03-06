package com.cmput301w17t08.moodr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Canopy on 2017-03-04.
 */

public class ElasticSearchUserController {
    private  static JestDroidClient client;

    // adds user to elasticsearch
    public static class addUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user:users) {
                Index index = new Index.Builder(user).index("cmput301w17t8").type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elasticsearch was not able to add user.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send user.");
                }
            }
            return null;
        }
    }

    // gets users from elasticsearch
    public static class GetUserTask extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected  ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            String query =  "{\"query\" : {\"term\" : { \"username\" : \"" +search_parameters[0] + "\" }}}";

            // Build the query
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t8")
                    .addType("user")
                    .build();

            try {
                // gets result
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.i("Error", "The seach query failed to find any user that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return users;
        }
    }

    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
