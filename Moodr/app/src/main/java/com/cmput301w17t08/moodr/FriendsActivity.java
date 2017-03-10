package com.cmput301w17t08.moodr;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by ZL on 3/4/2017.
 */
public class FriendsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final AppSectionsPagerAdapter adapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
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






    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
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
                    return new MapFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {return mNumOfTabs;}


    }

    public static class FriendsFragment extends Fragment {
        String search_string;
        boolean isSearching = false;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


            if (isSearching == false){
                View rootView = inflater.inflate(R.layout.fragment_friends_nosearch, container, false);
                final EditText searchBar = (EditText)rootView.findViewById(R.id.search_bar);
                ImageView searchIcon = (ImageView)rootView.findViewById(R.id.search_icon);

                searchIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isSearching = true;
                        search_string=searchBar.getText().toString();
                        //Search
                    }
                });

                return rootView;
            }else {
                View rootView = inflater.inflate(R.layout.fragment_friends_search,container,false);
                final EditText searchBar = (EditText)rootView.findViewById(R.id.search_bar);
                searchBar.setText(search_string);
                ImageView searchIcon = (ImageView)rootView.findViewById(R.id.search_icon);
                ImageView cancelIcon = (ImageView)rootView.findViewById(R.id.cancel_icon);

                searchIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isSearching = true;
                        search_string=searchBar.getText().toString();
                        //Search
                    }
                });

                cancelIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isSearching = false;
                        search_string="";
                        searchBar.setText("");
                        //refresh
                    }
                });
                return rootView;
            }


//            View rootView = new View(getActivity());
//            rootView = inflater.inflate(R.layout.fragment_friends_nosearch, container, false);
//
//            View l1 = rootView.findViewById(R.id.linear1);
//
//
//
////            LayoutInflater vi =(LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflater.inflate(R.layout.inflater,null);
//
//
//
//            ((LinearLayout)l1).addView(v);
//            return rootView;
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
