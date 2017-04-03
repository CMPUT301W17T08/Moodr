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
 * Handling elasticsearch user related queries
 */

public class ElasticSearchUserController {
    private static JestDroidClient client;

    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    // adds user to elasticsearch
    public static class AddUserTask extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... users) {
            verifySettings();

            String user_ID = null;

            for (User user : users) {
                Index index1 = new Index.Builder(user).index("cmput301w17t8").type("user").build();

                try {
                    DocumentResult result1 = client.execute(index1);
                    if (!result1.isSucceeded()) {
                        Log.i("Error", "Elasticsearch was not able to add user.");
                    } else {
                        user_ID = result1.getId();
                        user.setUser_Id(user_ID);
                        Index index2 = new Index.Builder(user).index("cmput301w17t8").type("user").id(user_ID).build();
                        try {
                            DocumentResult result2 = client.execute(index2);
                            if (!result1.isSucceeded()) {
                                Log.i("Error", "Elasticsearch was not able to add user.");
                            }
                        } catch (Exception e) {
                            Log.i("Error", "The application failed to build and send user.");
                        }
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send user.");
                }
            }
            return user_ID;
        }
    }

    // gets users from elasticsearch
    public static class GetUserTask extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            String query = "{\"query\" : {\"term\" : { \"name\" : \"" + search_parameters[0] + "\" }}}";

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
                } else {
                    Log.i("Error", "The search query failed to find any user that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return users;
        }
    }

    // add a user to the pending list
    public static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            User user = search_parameters[0];
            String user_ID = user.getUser_Id();

            Index index = new Index.Builder(user).index("cmput301w17t8").type("user").id(user_ID).build();

            try {
                DocumentResult result = client.execute(index);
                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch was not able to update user.");
                }
            } catch (Exception e) {
                Log.i("Error", "The application failed to build and send user.");
            }

            return null;
        }
    }

}