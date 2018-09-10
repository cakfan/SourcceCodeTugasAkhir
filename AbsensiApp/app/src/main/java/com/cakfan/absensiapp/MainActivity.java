package com.cakfan.absensiapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.utils.Utils;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String dataPesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout_id);
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.tambahFragment(new AbsenMasukFragment(), "Masuk");
        adapter.tambahFragment(new AbsenPulangFragment(), "Pulang");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        toolbar = findViewById(R.id.profileToolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        String uid = null;
        DrawerUtil.getDrawer(this, toolbar, user, database);

        dataPesan = getIntent().getStringExtra("pesannya");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        ImageView img = layout.findViewById(R.id.gambarPesan);
        img.setImageResource(R.mipmap.ic_launcher);
        TextView tks = layout.findViewById(R.id.teksPesan);
        tks.setText(dataPesan);

        if (!TextUtils.isEmpty(dataPesan)){
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL, 0,40);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
        //Toast.makeText(this, dataPesan,Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            kelogin();
        }
    }

    private void kelogin() {
        Intent klogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(klogin);
        finish();
    }
}
