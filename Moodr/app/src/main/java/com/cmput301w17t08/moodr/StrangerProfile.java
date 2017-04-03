package com.cmput301w17t08.moodr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * This activity is a stranger's profile. This shows no moods and only allows the user to follow
 * the user if not already pending. Loads one of two fragments depending on whether the user
 * is pending or a total stranger.
 * <p>
 * If this user has requested to follow the logged in user, it will give the option to accept
 * or decline instead.
 */
public class StrangerProfile extends AppCompatActivity {
    private boolean status;
    private Button follow;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_stranger_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        final String name = getIntent().getStringExtra("name");
        setTitle(name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        if (CurrentUserSingleton.getInstance().getUser().getPending().contains(name)) {
            // accept or decline
            transaction.add(PendingFragment.newInstance(name), null);
        } else {
            // add
            transaction.add(AddFragment.newInstance(name), null);
        }

        transaction.commit();
    }


    public static class PendingFragment extends Fragment {
        private static final String ARG_user = "username";
        private String user;

        public PendingFragment() {
        }

        public static PendingFragment newInstance(String username) {
            PendingFragment fragment = new PendingFragment();
            Bundle args = new Bundle();
            args.putString(ARG_user, username);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                user = getArguments().getString(ARG_user);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            final Activity activity = getActivity();

            Button accept = (Button) activity.findViewById(R.id.stranger_button1);
            accept.setText("Accept");

            Button decline = (Button) activity.findViewById(R.id.stranger_button2);
            decline.setText("Decline");

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptRequest(user);
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    declineRequest(user);
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
            });
        }

        public void acceptRequest(String user) {
            final String  name = user;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CurrentUserSingleton.getInstance().getUser().removePending(name);
                    CurrentUserSingleton.getInstance().getUser().addFriend(name);
                }
            });

            new ElasticSearchUserController.UpdateUserTask()
                    .execute(CurrentUserSingleton.getInstance().getUser());

            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(name);

            User u2 = new User();

            try{
                u2 = getUserTask.get().get(0);
            }
            catch (Exception e){
                // nothing.
            }

            u2.addFriend(CurrentUserSingleton.getInstance().getUser().getName());
            new ElasticSearchUserController.UpdateUserTask().execute(u2);
        }

        public void declineRequest(String user) {
            final String  name = user;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CurrentUserSingleton.getInstance().getUser().removePending(name);
                }
            });

            new ElasticSearchUserController.UpdateUserTask()
                    .execute(CurrentUserSingleton.getInstance().getUser());
        }

    }


    public static class AddFragment extends Fragment {
        private static final String ARG_user = "username";
        private String user;

        public AddFragment() {
        }

        public static AddFragment newInstance(String username) {
            AddFragment fragment = new AddFragment();
            Bundle args = new Bundle();
            args.putString(ARG_user, username);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                user = getArguments().getString(ARG_user);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            final Activity activity = getActivity();

            Button button2 = (Button) activity.findViewById(R.id.stranger_button2);
            button2.setVisibility(View.GONE);


            Button add = (Button) activity.findViewById(R.id.stranger_button1);
            if (!checkPending(user)) {
                add.setText("Add");
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addUser(user);
                        getActivity().finish();
                    }
                });
            } else {
                add.setText("Pending");
            }

            if (CurrentUserSingleton.getInstance().getUser().getFriends().contains(user)){
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("name", user);
                startActivity(intent);
                getActivity().finish();
            }

        }

        /**
         * Checks if there is already a pending request for the user.
         *
         * @param name of user pending
         * @return Boolean value. Whether if there is a pending request for the user.
         * @throws Exception
         */
        private boolean checkPending(String name) { // change to more specific exception later.
            // Check if app is connected to a network.
            ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null == activeNetwork) {
                Toast.makeText(getActivity(), "You are offline.", Toast.LENGTH_SHORT).show();
            } else {
                ElasticSearchUserController.GetUserTask getUserTask =
                        new ElasticSearchUserController.GetUserTask();
                getUserTask.execute(name.toLowerCase());
                try {
                    User user2 = getUserTask.get().get(0); // get first user from result
                    String currentUsername = CurrentUserSingleton.getInstance().getUser().getName();
                    return user2.getPending().contains(currentUsername);
                } catch (Exception e) {
                    Log.d("Error", "Unable to get user from elastic search");
                }
            }
            return false;
        }

        /**
         * adds pending request for a given user.
         *
         * @param name of user to send request to.
         * @throws Exception
         */
        private void addUser(String name) {
            // Check if app is connected to a network.
            ConnectivityManager cm = (ConnectivityManager) getActivity()
                    .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null == activeNetwork) {
                Toast.makeText(getActivity()
                        .getApplicationContext(), "Unable to send request when offline.", Toast.LENGTH_SHORT).show();
            } else {
                User user2 = new User();

                ElasticSearchUserController.GetUserTask getUserTask
                        = new ElasticSearchUserController.GetUserTask();
                getUserTask.execute(name);

                try {
                    user2 = getUserTask.get().get(0); // get first user from result
                } catch (Exception e) {
                    Log.d("Error", "Unable to get user from elastic search");
                }

                user2.addPending(CurrentUserSingleton.getInstance().getUser().getName());

                // update on elastic search
                ElasticSearchUserController.UpdateUserTask updateUserTask
                        = new ElasticSearchUserController.UpdateUserTask();
                updateUserTask.execute(user2);

            }
        }

    }
}
