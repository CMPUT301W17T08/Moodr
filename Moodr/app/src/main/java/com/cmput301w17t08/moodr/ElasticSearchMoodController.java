package com.cmput301w17t08.moodr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 *
 * ElasticSearchMoodController transfers data to/from elasticsearch and the application itself.
 *
 */

public class ElasticSearchMoodController {
    private  static JestDroidClient client;

    // adds mood to elasticsearch
    public static class AddMoodTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood:moods) {
                Index index = new Index.Builder(mood).index("cmput301w17t8").type("mood").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elasticsearch was not able to add mood.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send mood.");
                }
            }
            return null;
        }
    }

    // gets moods from elasticsearch
    public static class GetMoodTask extends AsyncTask<String, Void, ArrayList<Mood>> {

        @Override
        protected  ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            String query;
            ArrayList<Mood> moods = new ArrayList<Mood>();

            if (search_parameters.length == 2) {
                query = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"owner\": \""+ search_parameters[0] +"\"}}, {\"term\": {\"id\": \""+ search_parameters[1] +"\"}}]}}}";
            } else {
                query =  "{\"query\" : {\"term\" : { \"owner\" : \"" + search_parameters[0] + "\" }}}";
            }

            // Build the query
            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t8")
                    .addType("mood")
                    .build();

            try {
                // gets result
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Mood> foundMoods = result.getSourceAsObjectList(Mood.class);
                    moods.addAll(foundMoods);
                }
                else {
                    Log.i("Error", "The search query failed to find any mood that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return moods;
        }
    }

    // get a list of latest moods.
    public static class GetLatestMoodsTask extends AsyncTask<String, Void, ArrayList<Mood>> {

        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<Mood>();

            for (String sp : search_parameters) {
                String query = "{ \"query\" : { \"filtered\" : { \"filter\" : { \"term\" : { \"owner\" : \"" + sp + "\"}}}}, \"sort\" : { \"date\" : { \"order\" : \"desc\"}}, \"size\" : 1}";

                Search search = new Search.Builder(query)
                        .addIndex("cmput301w17t8")
                        .addType("mood")
                        .build();

                try {
                    // get result
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<Mood> foundMoods = result.getSourceAsObjectList(Mood.class);
                        moods.addAll(foundMoods);
                    } else {
                        Log.i("Error", "The search query failed to find any mood that matched.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }
            return moods;
        }
    }

    // delete moods from elasticsearch
    public static class DeleteMoodTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... search_parameters) {
            verifySettings();

            String query = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"owner\": \""+ search_parameters[0] +"\"}}, {\"term\": {\"id\": \""+ search_parameters[1] +"\"}}]}}}";

            // Build the query
            DeleteByQuery delete = new DeleteByQuery.Builder(query)
                    .addIndex("cmput301w17t8")
                    .addType("mood")
                    .build();

            try {
                client.execute(delete);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return null;
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
