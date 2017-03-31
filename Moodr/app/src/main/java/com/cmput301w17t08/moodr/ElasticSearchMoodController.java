package com.cmput301w17t08.moodr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;

/**
 *
 * ElasticSearchMoodController transfers data to/from elasticsearch and the application itself.
 *
 */

public class ElasticSearchMoodController {
    private  static JestDroidClient client;

    // adds mood to elasticsearch
    public static class AddMoodTask extends AsyncTask<Mood, Void, String> {

        @Override
        protected String doInBackground(Mood... moods) {
            verifySettings();

            String moodId = null;

            for (Mood mood:moods) {
                Index index1 = new Index.Builder(mood).index("cmput301w17t8").type("mood").build();

                try {
                    DocumentResult result1 = client.execute(index1);
                    if (!result1.isSucceeded()) {
                        Log.i("Error", "Elasticsearch was not able to add mood.");
                    }
                    else {
                        moodId = result1.getId();
                        mood.setId(moodId);
                        Index index2 = new Index.Builder(mood).index("cmput301w17t8").type("mood").id(moodId).build();
                        try {
                            DocumentResult result2 = client.execute(index2);
                            if (!result1.isSucceeded()) {
                                Log.i("Error", "Elasticsearch was not able to add mood.");
                            }
                        } catch (Exception e) {
                            Log.i("Error", "The application failed to build and send mood.");
                        }
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send mood.");
                }
            }
            return moodId;
        }
    }

    // gets moods from elasticsearch
    public static class GetMoodTask extends AsyncTask<String, Void, ArrayList<Mood>> {

        @Override
        protected  ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            String query;
            ArrayList<Mood> moods = new ArrayList<Mood>();

//            if (search_parameters.length == 2) {
//                query = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"owner\": \""+ search_parameters[0] +"\"}}, {\"term\": {\"id\": \""+ search_parameters[1] +"\"}}]}}}";
//            } else {
//                query =  "{\"query\" : {\"term\" : { \"owner\" : \"" + search_parameters[0] + "\" }}}";
//            }

            query =  "{\"query\" : {\"term\" : { \"owner\" : \"" + search_parameters[0] + "\" }}}";

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
    public static class GetLatestMoodsTask extends AsyncTask<ArrayList<String>, Void, ArrayList<Mood>> {

        @Override
        protected ArrayList<Mood> doInBackground(ArrayList<String>... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<Mood>();

            for (String sp : search_parameters[0]) {
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
            Collections.sort(moods, new Comparator<Mood>() {
                @Override
                public int compare(Mood mood, Mood t1) {
                    return t1.getDate().compareTo(mood.getDate());
                }
            });
            return moods;
        }
    }


    // get a list moods within 5km of current location.
    public static class GetNearByMoodsTask extends AsyncTask<Double, Void, ArrayList<Mood>> {

        @Override
        protected ArrayList<Mood> doInBackground(Double... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<Mood>();

            double current_latitude = search_parameters[0];
            double current_longitude = search_parameters[1];

            String query = "{ \"size\" : 0, \"aggs\" : { \"group\" : { \"terms\" : { \"field\" : \"owner\", \"size\" : 0 }, \"aggs\" : { \"group_docs\" : { \"terms\" : { \"field\" : \"date\", \"order\" : { \"_term\" : \"desc\" }, \"size\" : 1 }, \"aggs\" : { \"geof\" : { \"filter\" : { \"geo_distance\" : { \"distance\" : \"5\", \"distance_unit\" : \"km\", \"location\" : { \"lat\" : "+ current_latitude +", \"lon\" : "+ current_longitude +" }}}, \"aggs\" : { \"top_hits_geo\" : { \"top_hits\" : {}}}}}}}}}}";



            Search search = new Search.Builder(query)
                    .addIndex("cmput301w17t8")
                    .addType("mood")
                    .build();

            try {
                // get result
                SearchResult result = client.execute(search);

                if (result.isSucceeded()) {
                    List<TermsAggregation.Entry> agg = result.getAggregations().getTermsAggregation("group").getBuckets();
                    for (TermsAggregation.Entry entry : agg) {
                        List<TermsAggregation.Entry> agg1 = entry.getTermsAggregation("group_docs").getBuckets();
                        for (TermsAggregation.Entry entry1 : agg1) {
                            List<Mood> foundMoods = entry1.getFilterAggregation("geof").getTopHitsAggregation("top_hits_geo").getSourceAsObjectList(Mood.class);
                            moods.addAll(foundMoods);
                        }
                    }
                } else {
                    Log.i("Error", "The search query failed to find any mood that matched.");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }



            return moods;
        }
    }


    // delete moods from elasticsearch
    public static class DeleteMoodTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... search_parameters) {
            verifySettings();

            Mood mood = search_parameters[0];
            String moodId = mood.getId();

            // Build the query
            Delete delete = new Delete.Builder(moodId)
                    .index("cmput301w17t8")
                    .type("mood")
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

    // update a mood
    public static class UpdateMoodTask extends AsyncTask<Mood, Void, Void>{

        @Override
        protected Void doInBackground(Mood ... search_parameters) {
            verifySettings();

            Mood mood = search_parameters[0];
            String moodId = mood.getId();

            Index index = new Index.Builder(mood).index("cmput301w17t8").type("mood").id(moodId).build();

            try {
                DocumentResult result = client.execute(index);
                if (!result.isSucceeded()) {
                    Log.i("Error", "Elasticsearch was not able to update mood.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send mood.");
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
