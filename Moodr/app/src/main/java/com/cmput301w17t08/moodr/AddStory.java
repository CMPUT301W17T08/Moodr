package com.cmput301w17t08.moodr;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This fragment handles all the add story actions.
 */
public class AddStory extends Fragment {
    MyProfileActivity activity;
    String name;
    OnCompleteListener mListener;

    public AddStory() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        activity = (MyProfileActivity) getActivity();

        // hide floating buttons
        activity.findViewById(R.id.fab).setVisibility(View.GONE);
        activity.findViewById(R.id.go_to_map).setVisibility(View.GONE);
        activity.findViewById(R.id.notifications).setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            Toast.makeText(activity, "Cannot send story when offline.", Toast.LENGTH_SHORT).show();
            mListener.OnComplete();
        }

        else {
            // http://stackoverflow.com/questions/4016313/how-to-keep-an-alertdialog-open-after-button-onclick-is-fired
            // April 2 2017 4:43 pm

            // prompt user for story name
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.add_story_dialog, null);
            AlertDialog.Builder nameDialog = new AlertDialog.Builder(activity);
            nameDialog.setView(view);


            final EditText nameInput = (EditText) view.findViewById(R.id.story_name_field);
            nameDialog
                    .setCancelable(false)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                    mListener.OnComplete();
                                }
                            });

            final AlertDialog alertDialog = nameDialog.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            name = nameInput.getText().toString();
                            if (name.equals("")) {
                                Toast.makeText(activity, "Please enter a story name.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                activity.toggleCheckBoxes(true);
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_mood, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item1 = menu.findItem(R.id.storyButton);
        MenuItem item2 = menu.findItem(R.id.filter_menu);
        MenuItem item3 = menu.findItem(R.id.action_add_complete);
        MenuItem item4 = menu.findItem(R.id.action_add_cancel);

        if (item1 != null){
            item1.setVisible(false);
        }

        if (item2 != null){
            item2.setVisible(false);
        }

        if (item3 != null){
            item3.setVisible(true);
        }

        if (item4 != null){
            item4.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add_complete:
                ArrayList<Mood> moods = activity.getSelected();
                if (moods.size() != 0) {
                    // get friends
                        Story story = createStory(moods);
                        sendStory(story);
                        // notify activity
                        mListener.OnComplete();
                    }
                 else {
                    Toast.makeText(activity, "No moods select.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_add_cancel:
                // notify activity.
                mListener.OnComplete();
                break;
        }
        return false;
    }

    private Story createStory(ArrayList<Mood> moods) {
        Story story = new Story(CurrentUserSingleton.getInstance().getUser().getName(), name);
        story.addMoods(moods);
        return story;
    }

    private void sendStory(Story story1) {
        final Story story = story1;

        // http://stackoverflow.com/questions/3032342/arrayliststring-to-charsequence
        // 04-01-2017 10:55PM

        final ArrayList<String> friends = CurrentUserSingleton.getInstance().getUser().getFriends();
        CharSequence[] cs = friends.toArray(new CharSequence[friends.size()]);
        final ArrayList<String> selected = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Share with");
        builder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    selected.add(friends.get(i));
                } else {
                    selected.remove(friends.get(i));
                }
            }
        });
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected.clear();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                selected.clear();
            }
        });

        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (selected.size() == 0) {
                            Toast.makeText(activity, "No friends selected", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            if (null != activeNetwork) {
                                for (String friend : selected) {
                                    User user = new User();
                                    ElasticSearchUserController.GetUserTask getUserTask =
                                            new ElasticSearchUserController.GetUserTask();
                                    getUserTask.execute(friend);

                                    try {
                                        user = getUserTask.get().get(0);
                                    } catch (Exception e) {
                                        Log.i("Error", "Failed to get user from elastic search");
                                    }

                                    user.addStory(story);

                                    new ElasticSearchUserController.UpdateUserTask().execute(user);
                                }

                                Toast.makeText(activity, "Story sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Unable able to send story when offline.", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnCompleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }

    }

    public interface OnCompleteListener {
        public abstract void OnComplete();
    }
}
