package com.cmput301w17t08.moodr;



import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * The FriendsActivity class displays a list of current friends that are following you and
 * you are following back. It also shows list of pending friend requests. Either can be accessed
 * by pressing one of the tabs
 *
 */
public class FriendsActivity extends AppCompatActivity {


    public static AppSectionsPagerAdapter adapter;
    public static String search_string;
    public static ArrayList<String> searchResults;
    public static ArrayAdapter<String> resultAdapter;
    public static ArrayList<String> curFriends;
    public static ArrayList<String> Pending;
    public static ArrayList<String> modPending;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            return inflater.inflate(R.layout.fragment_friends, container, false);}

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            updateUser();
            ListView friendsList = (ListView) view.findViewById(R.id.curFriends_list);
            ListView pendingList = (ListView) view.findViewById(R.id.pending_list);

            curFriends = CurrentUserSingleton.getInstance().getUser().getFriends();
            Pending = CurrentUserSingleton.getInstance().getUser().getPending();
            modPending = pendingTomodPending(Pending);


            ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, curFriends);
            friendsList.setAdapter(friendsAdapter);

            ArrayAdapter<String> pendingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_activity_list_item, modPending);
            pendingList.setAdapter(pendingAdapter);

            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //go into friends profile
                    Intent intent = new Intent(getActivity(), Profile.class);
                    intent.putExtra("name", curFriends.get(position));
                    startActivity(intent);

                }
            });
            pendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    //go into friends profile
                    Intent intent = new Intent(getActivity(), StrangerProfile.class);
                    intent.putExtra("name", Pending.get(position));
                    startActivity(intent);

                }
            });

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

            final EditText searchBar = (EditText)view.findViewById(R.id.search_bar);
            searchBar.setText("");
            ImageView searchIcon = (ImageView)view.findViewById(R.id.search_icon);
            ImageView cancelIcon = (ImageView)view.findViewById(R.id.cancel_icon);
            searchResults = new ArrayList<String>();

            resultAdapter = new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,searchResults);
            ListView searchReturnList = (ListView) view.findViewById(R.id.search_return_list);
            searchReturnList.setAdapter(resultAdapter);

            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchBar.getText().toString().isEmpty()){
                        search_string="";
                        searchResults = new ArrayList<String>();
                        resultAdapter.notifyDataSetChanged();
                    }
                    else{
                        search_string=searchBar.getText().toString();
                        searchResults=searchUser(search_string);
                        resultAdapter.notifyDataSetChanged();

                    }
                }
            });

            cancelIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_string="";
                    searchBar.setText("");
                    searchResults = new ArrayList<String>();
                    resultAdapter.notifyDataSetChanged();
                }
            });



            searchReturnList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v,int position,long id){
                    // go into search return stranger profile
                    Intent intent = new Intent(getActivity(), StrangerProfile.class);
                    intent.putExtra("name", searchResults.get(position));
                    startActivity(intent);
                }
            });


        }
    }



    private static ArrayList<String>searchUser(String name){
        ArrayList<User> userList = new ArrayList<User>();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(name);

        try{
            userList = getUserTask.get();
        }
        catch(Exception e){
            Log.i("Error", "Error getting users out of async object");
        }
        ArrayList<String> userNameList = new ArrayList<>();
        for(User a:userList){
            userNameList.add(a.getName());
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

    private static void updateUser(){
        // populate all current user info here.
        ElasticSearchUserController.GetUserTask getUserTask
                = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(CurrentUserSingleton.getInstance().getUser().getName());
        User user = new User();
        try{
            user = getUserTask.get().get(0);
            Log.d("USERNAME", user.getName());
        }
        catch(Exception e){
            Log.d("ERROR", "Error getting user from elastic search");
        }
        User currentUser = CurrentUserSingleton.getInstance().getUser();
        currentUser.setName(user.getName());
        currentUser.setUser_Id(user.getUser_Id());
        currentUser.setFriends(user.getFriends());
        currentUser.setPending(user.getPending());


    }



}