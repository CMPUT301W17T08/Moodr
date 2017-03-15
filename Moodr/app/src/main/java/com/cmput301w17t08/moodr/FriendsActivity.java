package com.cmput301w17t08.moodr;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 *
 */
public class FriendsActivity extends AppCompatActivity {

    public static boolean isSearching = false;
    public static AppSectionsPagerAdapter adapter;
    public static String search_string;
    public static ArrayList<String> searchResults;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


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
                        //Search
                        //Populate searchResults
                        // refresh

                        adapter.notifyDataSetChanged();
                    }
                }
            });

            ListView friendsList = (ListView) view.findViewById(R.id.curFriends_list);
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
                }
            });

            ArrayList<String> curFriends = CurrentUserSingleton.getInstance().getUser().getFriends();
            ArrayList<String> Pending = CurrentUserSingleton.getInstance().getUser().getPending();

            ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(getActivity(),R.layout.curfriends_list_items,curFriends);
            friendsList.setAdapter(friendsAdapter);

            ArrayAdapter<String> pendingAdapter = new ArrayAdapter<String>(getActivity(),R.layout.pending_list_item,Pending);
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

            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchBar.getText().toString().isEmpty()){
                        isSearching=false;
                        search_string="";
                        searchResults = new ArrayList<String>();
                        //refresh
                        adapter.notifyDataSetChanged();
                    }else{
                    isSearching = true;
                    search_string=searchBar.getText().toString();
                    adapter.notifyDataSetChanged();
                    //Search
                    // populate searchResults
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
                    //refresh
                    //notify list changed
                    adapter.notifyDataSetChanged();
                }
            });

            ListView searchReturnList = (ListView) view.findViewById(R.id.search_return_list);

            searchReturnList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v,int position,long id){
                    // go into search return stranger profile
                }
            });

            ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(getActivity(),R.layout.search_return_list_item,searchResults);
//                searchReturnList.setAdapter(resultAdapter);
        }
    }


    public static class MapFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            return rootView;
        }
    }


}
