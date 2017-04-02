package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewStoryActivity extends AppCompatActivity {
    private ArrayList<Mood> moodList;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        Intent intent = getIntent();
        Story story = (Story) intent.getSerializableExtra("story");
        setTitle(story.getOwner() + "-" + story.getName());

        moodList = story.getMoodList();

        // Create the adapter that will return a fragment for each mood
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a EndFragment (defined as a static inner class below).
            if (position < moodList.size()) {
                return ViewMoodFragment.newInstance(moodList.get(position));
            }
            else return EndFragment.newInstance();
        }

        @Override
        public int getCount() {
            return moodList.size()+1;
        }
    }

    public static class EndFragment extends Fragment {
        public EndFragment() {
        }

        @Override
        public void onStart( ) {
            super.onStart();

            Button button = (Button) getView().findViewById(R.id.story_finish);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        public static EndFragment newInstance() {
            EndFragment fragment = new EndFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.story_end, container, false);
            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }

    }
}
