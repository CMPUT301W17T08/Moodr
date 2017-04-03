package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * This activity allows the user to view a specified story. The user can swipe through all moods in
 * the story. The story is marked as read and removed afterwards.
 */

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
     * This fragment allows the user to exit the activity, after all moods in the story have been
     * seen.
     */

    public static class EndFragment extends Fragment {
        public EndFragment() {
        }

        public static EndFragment newInstance() {
            EndFragment fragment = new EndFragment();
            return fragment;
        }

        @Override
        public void onStart() {
            super.onStart();

            Button button = (Button) getView().findViewById(R.id.story_finish);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.story_end, container, false);
            return rootView;
        }

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
            } else return EndFragment.newInstance();
        }

        @Override
        public int getCount() {
            return moodList.size() + 1;
        }
    }
}
