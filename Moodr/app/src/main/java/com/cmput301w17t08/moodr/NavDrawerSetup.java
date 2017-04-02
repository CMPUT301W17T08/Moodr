package com.cmput301w17t08.moodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    public NavDrawerSetup(AppCompatActivity activity, Toolbar toolbar){
        this.activity = activity;
        this.name =  CurrentUserSingleton.getInstance().getUser().getName();
        this.toolbar = toolbar;
    }

    public void setupNav(){
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
                .withDelayOnDrawerClose(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(R.drawable.ic_home),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Latest").withIcon(R.drawable.ic_latest),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Friends").withIcon(R.drawable.ic_friends),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Near Me").withIcon(R.drawable.ic_map),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Story").withIcon(R.drawable.heart),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(6).withName("Log Out").withIcon(R.drawable.ic_logout)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        drawer.closeDrawer();
                        switch (position){
                            case 1:
                                if (!activity.equals(MyProfileActivity.class)){
                                intent = new Intent(activity, MyProfileActivity.class);
                                activity.startActivity(intent);}
                                break;
                            case 2:
                                intent = new Intent(activity, LatestActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(activity, FriendsActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(activity, MapsActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 5:
                                intent = new Intent(activity, StoryActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 7:
                                CurrentUserSingleton.getInstance().reset();
                                intent = new Intent(activity, LoginActivity.class);
                                intent.putExtra("logout", 1);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(intent);
                                break;
                        }
                        return true;
                    }
                })
                .build();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}
