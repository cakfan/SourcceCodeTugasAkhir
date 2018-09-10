package com.cakfan.absensiapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AbsenMasukFragment extends Fragment {

    private View view;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;
    private TextView tv, tvSenin, tvSelasa, tvRabu, tvKamis, tvJumat, tvSabtu;
    private String rfidnya, senin, selasa, rabu, kamis, jumat, sabtu;
    private ProgressBar loading;

    public AbsenMasukFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_absen_masuk, container, false);
        loading = view.findViewById(R.id.loadingbar);
        tv = view.findViewById(R.id.tv_notif);
        tvSenin = view.findViewById(R.id.tvSenin);
        tvSelasa = view.findViewById(R.id.tvSelasa);
        tvRabu = view.findViewById(R.id.tvRabu);
        tvKamis = view.findViewById(R.id.tvKamis);
        tvJumat = view.findViewById(R.id.tvJumat);
        tvSabtu = view.findViewById(R.id.tvSabtu);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();


        String uid = null;
        if (user == null) {
            kelogin();
        } else {
            uid = user.getUid();
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rfidnya = dataSnapshot.child("rfid_siswa").getValue().toString();
                    database = FirebaseDatabase.getInstance().getReference().child("absensi").child(rfidnya);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            loading.setVisibility(View.GONE);
                            if (dataSnapshot.exists()) {
                                Log.d(getTag(), "onDataChange: " + dataSnapshot);
                                senin = dataSnapshot.child("Senin").getValue().toString();
                                selasa = dataSnapshot.child("Selasa").getValue().toString();
                                rabu = dataSnapshot.child("Rabu").getValue().toString();
                                kamis = dataSnapshot.child("Kamis").getValue().toString();
                                jumat = dataSnapshot.child("Jumat").getValue().toString();
                                sabtu = dataSnapshot.child("Sabtu").getValue().toString();

                                if (!TextUtils.isEmpty(senin)) {
                                    tvSenin.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSenin.setText("Senin: " + senin);
                                } else {
                                    tvSenin.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSenin.setText("Senin: Tidak ada data.");
                                }
                                if (!TextUtils.isEmpty(selasa)) {
                                    tvSelasa.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSelasa.setText("Selasa: " + selasa);
                                } else {
                                    tvSelasa.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSelasa.setText("Selasa: Tidak ada data.");
                                }
                                if (!TextUtils.isEmpty(rabu)) {
                                    tvRabu.setTextColor(getResources().getColor(R.color.colorText));
                                    tvRabu.setText("Rabu: " + rabu);
                                } else {
                                    tvRabu.setTextColor(getResources().getColor(R.color.colorText));
                                    tvRabu.setText("Rabu: Tidak ada data.");
                                }
                                if (!TextUtils.isEmpty(kamis)) {
                                    tvKamis.setTextColor(getResources().getColor(R.color.colorText));
                                    tvKamis.setText("Kamis: " + kamis);
                                } else {
                                    tvKamis.setTextColor(getResources().getColor(R.color.colorText));
                                    tvKamis.setText("Kamis: Tidak ada data.");
                                }
                                if (!TextUtils.isEmpty(jumat)) {
                                    tvJumat.setTextColor(getResources().getColor(R.color.colorText));
                                    tvJumat.setText("Jumat: " + kamis);
                                } else {
                                    tvJumat.setTextColor(getResources().getColor(R.color.colorText));
                                    tvJumat.setText("Jumat: Tidak ada data.");
                                }
                                if (!TextUtils.isEmpty(sabtu)) {
                                    tvSabtu.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSabtu.setText("Sabtu: " + sabtu);
                                } else {
                                    tvSabtu.setTextColor(getResources().getColor(R.color.colorText));
                                    tvSabtu.setText("Sabtu: Tidak ada data.");
                                }
                            } else {
                                Log.d(getTag(), "onDataChange: kosong");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            loading.setVisibility(View.GONE);

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        tv.setVisibility(View.GONE);

        return view;
    }

    private void kelogin() {
        Intent klogin = new Intent(getContext(), LoginActivity.class);
        startActivity(klogin);
        getActivity().finish();
    }

}
