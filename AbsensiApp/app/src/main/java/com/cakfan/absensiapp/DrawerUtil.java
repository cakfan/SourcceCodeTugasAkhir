package com.cakfan.absensiapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Map;


public class DrawerUtil {

    public static void getDrawer(final Activity activity, Toolbar toolbar, final FirebaseUser user, final DatabaseReference database) {
        //if you want to update the items at a later time it is recommended to keep it in a variable
//        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
//        drawerEmptyItem.withEnabled(false);

        PrimaryDrawerItem drawerItemManagePlayers = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.absensi).withIcon(R.drawable.ic_assignment_black_24dp);
        PrimaryDrawerItem drawerItemManagePlayersTournaments = new PrimaryDrawerItem()
                .withIdentifier(2).withName(R.string.profile).withIcon(R.drawable.ic_person_black_24dp);


        SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.settings).withIcon(R.drawable.ic_settings_black_24dp);
        SecondaryDrawerItem drawerItemAbout = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.about).withIcon(R.drawable.ic_smartphone_black_24dp);
        SecondaryDrawerItem drawerItemHelp = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.help).withIcon(R.drawable.ic_help_black_24dp);
        SecondaryDrawerItem drawerKeluar = new SecondaryDrawerItem().withIdentifier(6)
                .withName(R.string.keluar).withIcon(R.drawable.ic_power_settings_new_black_24dp);




        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        //drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
                        drawerItemManagePlayers,
                        drawerItemManagePlayersTournaments,
                        new DividerDrawerItem(),
//                        drawerItemAbout,
//                        drawerItemSettings,
//                        drawerItemHelp,
                        drawerKeluar
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1 && !(activity instanceof MainActivity)) {
                            // load tournament screen
                            Intent intent = new Intent(activity, MainActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2) {
                            Intent intent = new Intent(activity, ProfileActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 6){
                            String uid = user.getUid();
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("token_id","");
                            database.child("users").child(uid).updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent klogin = new Intent(activity, LoginActivity.class);
                                    view.getContext().startActivity(klogin);
                                    activity.finish();
                                }
                            });
                        }
                        return true;
                    }
                })
                .build();

        //result.addStickyFooterItem(new PrimaryDrawerItem().withName("Created by Cakfan"));

    }
}
