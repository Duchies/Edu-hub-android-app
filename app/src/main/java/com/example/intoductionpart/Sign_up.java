package com.example.intoductionpart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.intoductionpart.Activities.HomeActivity;
import com.example.intoductionpart.Activities.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_up extends AppCompatActivity {

    private EditText login_username, loin_password;
    private Button login_btn;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
   // private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        login_username = findViewById(R.id.login_username);
        loin_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        loginProgressBar = findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();

        HomeActivity = new Intent(this, com.example.intoductionpart.MainActivityNavigationDrawer.class);

        loginProgressBar.setVisibility(View.INVISIBLE);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginProgressBar.setVisibility(View.VISIBLE);

                final String mail = login_username.getText().toString();
                final String password = loin_password.getText().toString();


                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify Fields");
                    loginProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    signIn(mail, password);
                }

            }
        });

    }

    private void signIn(String mail, String password) {


        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    loginProgressBar.setVisibility(View.INVISIBLE);
                    updateUI();

                } else {
                    showMessage(task.getException().getMessage());

                    loginProgressBar.setVisibility(View.INVISIBLE);
                }


            }
        });


    }

    private void updateUI() {

        startActivity(HomeActivity);
        finish();

    }

    private void showMessage(String message) {


        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //user is already connected  so we need to redirect him to home page
            updateUI();

        }
    }

    public void registoPageSwitch(View view) {

        Intent intent  = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}