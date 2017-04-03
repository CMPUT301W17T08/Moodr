package com.cmput301w17t08.moodr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * A class to set up the navigation drawer for any activities that require it.
 */

public class NavDrawerSetup {
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private String name;
    private Drawer drawer;

    public NavDrawerSetup(AppCompatActivity activity, Toolbar toolbar) {
        this.activity = activity;
        this.name = CurrentUserSingleton.getInstance().getUser().getName();
        this.toolbar = toolbar;
    }

    public void setupNav() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("My Profile");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("");

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(
                        new ProfileDrawerItem().withName(name)).withSelectionListEnabledForSingleProfile(false).withHeaderBackground(R.drawable.gradient_background).build();

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(R.drawable.ic_home),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Latest").withIcon(R.drawable.ic_latest),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Friends").withIcon(R.drawable.ic_friends),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Near Me").withIcon(R.drawable.ic_map),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Log Out").withIcon(R.drawable.ic_logout)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        drawer.closeDrawer();
                        switch (position) {
                            case 1:
                                intent = new Intent(activity, MyProfileActivity.class);
                                activity.startActivity(intent);
                                break;

                            case 2:
                                if (!activity.getClass().equals((LatestActivity.class))) {
                                    intent = new Intent(activity, LatestActivity.class);
                                    activity.startActivity(intent);
                                }
                                break;

                            case 3:
                                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                if (null != activeNetwork) {
                                    if (!activity.getClass().equals((FriendsActivity.class))) {
                                        intent = new Intent(activity, FriendsActivity.class);
                                        activity.startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(activity, "Unable to go to friend page when offline.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 4:
                                if (!activity.getClass().equals((MapsActivity.class))) {
                                    intent = new Intent(activity, MapsActivity.class);
                                    activity.startActivity(intent);
                                }

                                break;

                            case 6:
                                // http://stackoverflow.com/questions/7075349/android-clear-activity-stack
                                // April 2 2017 4:25 am

                                if (CurrentUserSingleton.getInstance().getMyOfflineActions().getSize() > 0) {
                                    DisableLogout();
                                } else {
                                    logout();
                                }
                                break;
                        }
                        return true;
                    }
                })
                .build();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void DisableLogout() {

        new android.app.AlertDialog.Builder(activity)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Logout failed! Moods not synced!")
                .setMessage("Moodr did not save the changes you made while offline! Get internet connection and refresh the page!")
                .setNegativeButton("Gotcha!", null)
                .create().show();
    }

    private void logout() {
        CurrentUserSingleton.getInstance().reset();
        new SaveSingleton(activity).SaveSingletons(); // save singleton to disk.
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("logout", 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(activity, "Logged out!", Toast.LENGTH_SHORT).show();
        activity.startActivity(intent);
    }





//    private void LogoutDisable() {
//
//        new android.app.AlertDialog.Builder(activity)
//                .setIcon(R.drawable.ic_warning_black_24dp)
//                .setTitle("Logout failed! Moods not synced!")
//                .setMessage("Moodr did not save changes you made while offline!")
//                .setNegativeButton("Gotcha!", null);
//    }
//
//    private void logout() {
//        CurrentUserSingleton.getInstance().reset();
//        Intent intent = new Intent(activity, LoginActivity.class);
//        intent.putExtra("logout", 1);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Toast.makeText(activity, "Logged out!", Toast.LENGTH_SHORT).show();
//        activity.startActivity(intent);
//    }
}
