package com.cmput301w17t08.moodr;



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

    public static boolean isSearching = false;
    public static AppSectionsPagerAdapter adapter;
    public static String search_string;
    public static ArrayList<String> searchResults;
    public static ArrayAdapter<String> resultAdapter;
    public static ArrayList<String> curFriends;
    public static ArrayList<String> Pending;
    public static ArrayList<String> modPending;
    public static ArrayAdapter<String> pendingAdapter;
    public static ArrayAdapter<String> friendsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        isSearching = false;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
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
                    if(isSearching==false){
                    return new FriendsFragment();}
                    else{return new FriendSearchedFragment();}
                case 1:
                    return new MapFragment();
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
            return inflater.inflate(R.layout.fragment_friends_nosearch, container, false);}

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view,savedInstanceState);
            final EditText searchBar = (EditText)view.findViewById(R.id.search_bar);
            ImageView searchIcon = (ImageView)view.findViewById(R.id.search_icon);

            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!searchBar.getText().toString().isEmpty()) {
                        isSearching = true;
                        search_string = searchBar.getText().toString();
                        searchResults = searchUser(search_string);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            final ListView friendsList = (ListView) view.findViewById(R.id.curFriends_list);
            ListView pendingList = (ListView) view.findViewById(R.id.pending_list);

            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                    //go into friends profile

                }
            });

            pendingList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v,int position,long id){
                    // go into pending stranger profile

                    // for testing purpose this will be accepting friends
                     pendingToFriends(Pending.get(position));

//                    this is to update the lists
                    curFriends = CurrentUserSingleton.getInstance().getUser().getFriends();
                    Pending = CurrentUserSingleton.getInstance().getUser().getPending();
                    modPending = pendingTomodPending(Pending);
                    pendingAdapter.notifyDataSetChanged();
                    friendsAdapter.notifyDataSetChanged();

                }
            });

            updateUser();
            curFriends =new ArrayList<String>();
            Pending =new ArrayList<String>();

            curFriends = CurrentUserSingleton.getInstance().getUser().getFriends();
            Pending = CurrentUserSingleton.getInstance().getUser().getPending();
            modPending = pendingTomodPending(Pending);



//            btw all the other list items are not in use, but they are still in layout just in case, remember to clean for final version
            friendsAdapter = new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,curFriends);
            friendsList.setAdapter(friendsAdapter);

            pendingAdapter = new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,modPending);
            pendingList.setAdapter(pendingAdapter);

        }
    }

    public static class FriendSearchedFragment extends Fragment{


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            return inflater.inflate(R.layout.fragment_friends_search, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            super.onViewCreated(view,savedInstanceState);

            final EditText searchBar = (EditText)view.findViewById(R.id.search_bar);
            searchBar.setText(search_string);
            ImageView searchIcon = (ImageView)view.findViewById(R.id.search_icon);
            ImageView cancelIcon = (ImageView)view.findViewById(R.id.cancel_icon);

            resultAdapter = new ArrayAdapter<String>(getActivity(),R.layout.friends_activity_list_item,searchResults);
            ListView searchReturnList = (ListView) view.findViewById(R.id.search_return_list);
            searchReturnList.setAdapter(resultAdapter);

            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchBar.getText().toString().isEmpty()){
                        isSearching=false;
                        search_string="";
                        searchResults = new ArrayList<String>();
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        isSearching = true;
                        search_string=searchBar.getText().toString();
                        searchResults=searchUser(search_string);
                        adapter.notifyDataSetChanged();
                        resultAdapter.notifyDataSetChanged();

                    }
                }
            });

            cancelIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSearching = false;
                    search_string="";
                    searchBar.setText("");
                    searchResults = new ArrayList<String>();
                    adapter.notifyDataSetChanged();
                }
            });



            searchReturnList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v,int position,long id){
                    // go into search return stranger profile
//                    for testing purpose this will be sending request
                    addPending(searchResults.get(position));
                }
            });


        }
    }


    public static class MapFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            return rootView;
        }
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

        // populate all current user's mood
        ElasticSearchMoodController.GetMoodTask getMoodTask
                = new ElasticSearchMoodController.GetMoodTask();
        ArrayList<Mood> moods = new ArrayList<>();
        getMoodTask.execute(user.getName());
        try{
            moods.addAll(getMoodTask.get());
            Collections.sort(moods, new Comparator<Mood>() {
                @Override
                public int compare(Mood mood, Mood t1) {
                    return t1.getDate().compareTo(mood.getDate());
                }
            });
        }
        catch(Exception e){
            Log.d("Error", "Error getting moods from elastic search.");
        }
        MoodList userMoods = CurrentUserSingleton.getInstance().getMyMoodList();
        userMoods.setListOfMoods(moods);
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

//    these methods needs constrain, such as you can only add a search result to pending if they are not already in pending or friends
    private static void addPending(String name){
        try {
            ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            User user = new User();
            getUserTask.execute(name);
            user = getUserTask.get().get(0);
            updateUserTask.execute(user);
            user.addPending(CurrentUserSingleton.getInstance().getUser().getName());
            updateUserTask.equals(CurrentUserSingleton.getInstance().getUser());
        }catch (Exception e){
            Log.i("Error","Error adding pending");
        }
    }

    private static void pendingToFriends(String name){
        try {
            ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
            updateUserTask.execute(CurrentUserSingleton.getInstance().getUser());
            CurrentUserSingleton.getInstance().getUser().removePending(name);
            CurrentUserSingleton.getInstance().getUser().addFriend(name);
            updateUserTask.equals(CurrentUserSingleton.getInstance().getUser());
        }catch (Exception e){
            Log.i("Error","Error moving from pending to friends");
        }
    }

}
