package com.cakfan.absensiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;

    private TextInputEditText email,password;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
//        user = auth.getCurrentUser();

        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String in_email = email.getText().toString();
                String in_pass = password.getText().toString();
                if (!TextUtils.isEmpty(in_email)&&!TextUtils.isEmpty(in_pass)){
                    //btnLogin.setVisibility(View.INVISIBLE);
                    btnLogin.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(in_email,in_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                user = auth.getCurrentUser();
                                final String uid = user.getUid();
                                //Toast.makeText(LoginActivity.this,uid,Toast.LENGTH_LONG).show();

                                String token_id = FirebaseInstanceId.getInstance().getToken();

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id",token_id);
                                database.child("users").child(uid).updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        kemain();
                                        progressBar.setVisibility(View.GONE);
                                        //btnLogin.setVisibility(View.VISIBLE);
                                        btnLogin.setEnabled(true);
                                    }
                                });



                            } else {
                                String e = task.getException().getMessage();
                                progressBar.setVisibility(View.GONE);
                                //btnLogin.setVisibility(View.VISIBLE);
                                btnLogin.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        user = auth.getCurrentUser();
        super.onStart();
        if (user != null){
            kemain();
        }
    }

    private void kemain() {
        Intent kmain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(kmain);
        finish();
    }
}
