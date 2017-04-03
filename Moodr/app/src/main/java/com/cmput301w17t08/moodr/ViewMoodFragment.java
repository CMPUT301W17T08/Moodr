package com.cmput301w17t08.moodr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * This fragment shows details for individual moods. this is used to display moods when clicked on
 * profile pages, latest list, and in viewing stories.
 */
public class ViewMoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Mood mood;

    public ViewMoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewMoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewMoodFragment newInstance(Mood param1) {
        ViewMoodFragment fragment = new ViewMoodFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bitmap decodeImage(String imageString) {
        try {
            byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mood = (Mood) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_mood, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMood(mood);
    }

    protected void loadMood(Mood mood) {
        View view = getView();

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.frame);
        TextView mood_name = (TextView) view.findViewById(R.id.viewMoodMood);
        ImageView mood_icon = (ImageView) view.findViewById(R.id.viewMoodIcon);
        TextView date = (TextView) view.findViewById(R.id.viewMoodDate);
        TextView social = (TextView) view.findViewById(R.id.viewMoodSocialSituation);
        TextView trigger = (TextView) view.findViewById(R.id.viewMoodTrigger);
        TextView location = (TextView) view.findViewById(R.id.viewMoodLocation);
        ImageView image = (ImageView) view.findViewById(R.id.viewMoodImage);
        Bitmap imageToDisplay;

        // set title
        getActivity().setTitle(mood.getUsername());

        // set background color
        layout.setBackgroundColor(mood.getEmotion().getColor());

        // set mood's name
        mood_name.setText(mood.getEmotion().getName());

        // set emoticon
        mood_icon.setImageResource(mood.getEmotion().getEmoticon());

        // set date
        // date needs to be converted to a string
        java.text.DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US);
        date.setText(dateFormat.format(mood.getDate()));

        // set situation
        String situation = mood.getSituation();
        if (situation != null && situation.length() > 0) {
            social.setText("Social Situation: " + situation);
        }

        // set trigger
        String trig = mood.getTrigger();
        if (trig != null && trig.length() > 0) {
            trigger.setText("Trigger: " + trig);
        }

        // set location
        Coordinate loc = mood.getLocation();
        if (loc != null) {
            location.setText("Location:" + loc.getLat().toString() + " " + loc.getLon().toString());
        }

        // set image
        String imgURL = mood.getImgUrl();
        imageToDisplay = decodeImage(imgURL);
        if (imgURL != null) {
            image.setImageBitmap(imageToDisplay);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMood(mood);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}