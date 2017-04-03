package com.cmput301w17t08.moodr;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * The FriendsActivity class displays a list of current friends that are following you and
 * you are following back. It also shows list of pending friend requests. Either can be accessed
 * by pressing one of the tabs
 *
 * NOTE: there is a bug in the adapter not refreshing the friends and pending lists when accepting
 * a request. May cause the app to crash if an item is clicked afterwards.
 */

public class FriendsActivity extends AppCompatActivity {

    public static AppSectionsPagerAdapter adapter;

    public static ArrayList<String> searchResults;

    public static ArrayList<String> curFriends;
    public static ArrayList<String> Pending;
    public static ArrayList<String> modPending;
    public static EditText searchBar;
    public static ListView searchReturnList;
    public static ArrayAdapter<String> friendsAdapter;
    public static ArrayAdapter<String> pendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new NavDrawerSetup(this, toolbar).setupNav();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }




    public static class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public AppSectionsPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new FriendsFragment();
                case 1:
                    return new SearchFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            return POSITION_NONE;
        }

        @Override
        public int getCount() {return mNumOfTabs;}

    }


    public static class FriendsFragment extends Fragment {
        ListView friendsList;
        ListView pendingList;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            return inflater.inflate(R.layout.fragment_friends, container, false);}

        @Override
        public void onResume(){
            super.onResume();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateUser(getActivity());
                    friendsList = (ListView) getView().findViewById(R.id.curFriends_list);
                    pendingList = (ListView) getView().findViewById(R.id.pending_list);
                    friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, curFriends);
                    friendsList.setAdapter(friendsAdapter);

                    pendingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, modPending);
                    pendingList.setAdapter(pendingAdapter);
                }
            }, 1000);



        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            updateUser(getActivity());
            friendsList = (ListView) view.findViewById(R.id.curFriends_list);
            pendingList = (ListView) view.findViewById(R.id.pending_list);






            friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, curFriends);
            friendsList.setAdapter(friendsAdapter);

            pendingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, modPending);
            pendingList.setAdapter(pendingAdapter);

            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //go into friends profile
                    Intent intent = new Intent(getActivity(), Profile.class);
                    intent.putExtra("name", curFriends.get(position));
                    startActivityForResult(intent, 1);
                }
            });
            pendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //go into friends profile
                    Intent intent = new Intent(getActivity(), StrangerProfile.class);
                    intent.putExtra("name", Pending.get(position));
                    startActivityForResult(intent, 2);
                }
            });

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendsAdapter.notifyDataSetChanged();
                            pendingAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            else if (requestCode == 2){
                if (resultCode == RESULT_OK){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pendingAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }

    }


    public static class SearchFragment extends Fragment{


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            return inflater.inflate(R.layout.fragment_search, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view,savedInstanceState);

            searchBar = (EditText)view.findViewById(R.id.search_bar);
            ImageView searchIcon = (ImageView)view.findViewById(R.id.search_icon);
            ImageView cancelIcon = (ImageView)view.findViewById(R.id.cancel_icon);

            searchReturnList = (ListView) view.findViewById(R.id.search_return_list);




            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String search_string=searchBar.getText().toString();
                    searchResults=searchUser(search_string, getActivity());

                    searchResults.removeAll(Collections.singleton(null));

                    searchReturnList.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,searchResults));


                }
            });

            cancelIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    searchBar.setText("");
                    searchResults = new ArrayList<String>();
                    searchReturnList.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,searchResults));
                }
            });



            searchReturnList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v,int position,long id){
                    // go into search return stranger profile
                    Intent intent = new Intent(getActivity(), StrangerProfile.class);
                    intent.putExtra("name", searchResults.get(position));
                    startActivityForResult(intent, 1);
                }
            });


        }
    }


    private static ArrayList<String> searchUser(String name, Context context){
        ArrayList<User> userList = new ArrayList<User>();
        ArrayList<String> userNameList = new ArrayList<>();


        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            try {
                getUserTask.execute(name);
                userList = getUserTask.get();
            } catch (Exception e) {
                Log.i("Error", "Error getting users out of async object");
            }
            for (User a : userList) {
                userNameList.add(a.getName());
            }
        } else {
            Toast.makeText(context, "You are offline.", Toast.LENGTH_SHORT).show();
        }

        return userNameList;
    }

    private static ArrayList<String> pendingTomodPending(ArrayList<String> P){
        ArrayList<String> B = new ArrayList<String>();
        for(String a:P){
            B.add("Pending: "+ a);
        }
        return B;
    }

    private static void updateUser(Context context){
        // populate all current user info here.
        // Check if app is connected to a network.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            ElasticSearchUserController.GetUserTask getUserTask
                    = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(CurrentUserSingleton.getInstance().getUser().getName());
            User user = new User();
            try {
                user = getUserTask.get().get(0);
                Log.d("USERNAME", user.getName());
            } catch (Exception e) {
                Log.d("ERROR", "Error getting user from elastic search");
            }
            User currentUser = CurrentUserSingleton.getInstance().getUser();
            currentUser.setName(user.getName());
            currentUser.setUser_Id(user.getUser_Id());
            currentUser.setFriends(user.getFriends());
            currentUser.setPending(user.getPending());

            curFriends = CurrentUserSingleton.getInstance().getUser().getFriends();

            curFriends.removeAll(Collections.singleton(null));
            Pending = CurrentUserSingleton.getInstance().getUser().getPending();


            Pending.removeAll(Collections.singleton(null));
            modPending = pendingTomodPending(Pending);
            new SaveSingleton(context).SaveSingletons(); // save singleton to disk.
        } else {
            Toast.makeText(context, "You are offline.", Toast.LENGTH_SHORT).show();
        }

    }



}