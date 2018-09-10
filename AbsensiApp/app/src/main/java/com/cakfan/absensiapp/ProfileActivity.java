package com.cakfan.absensiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;

    private Toolbar toolbar;
    private TextView namaSiswa,alamatSiswa,rfidSiswa,namaWali;
    private CircleImageView imgProfile;

    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loading = findViewById(R.id.loadingbar2);
        toolbar = findViewById(R.id.profileToolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaSiswa = findViewById(R.id.namaSiswa);
        alamatSiswa = findViewById(R.id.alamatSiswa);
        rfidSiswa = findViewById(R.id.rfidSiswa);
        imgProfile = findViewById(R.id.gbrProfil);
        namaWali = findViewById(R.id.namaWali);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                String nama_siswa = dataSnapshot.child("nama_siswa").getValue().toString();
                String nama_wali = dataSnapshot.child("nama").getValue().toString();
                String alamat = dataSnapshot.child("alamat").getValue().toString();
                String rfid = dataSnapshot.child("rfid_siswa").getValue().toString();
                String gambar = dataSnapshot.child("gambar").getValue().toString();

                namaSiswa.setText(nama_siswa);
                Picasso.get().load(gambar).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imgProfile);
                alamatSiswa.setText(alamat);
                rfidSiswa.setText(rfid);
                namaWali.setText(nama_wali);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
