package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EndFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    OnCompleteListener mListener;

    public EndFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    @Override
    public void onStart( ) {
        super.onStart();

        Button button = (Button) getView().findViewById(R.id.story_finish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnComplete();
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

        try {
            mListener = (EndFragment.OnCompleteListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }

    }

    public interface OnCompleteListener {
        public abstract void OnComplete();
    }

}