package com.example.intoductionpart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intoductionpart.Activities.HomeActivity;
import com.example.intoductionpart.Activities.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;
////////////////////////////////////////////////////main class////////////////////////
public class Sign_up extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123 ;
    private EditText login_username, loin_password;
    private Button login_btn;
    private ProgressBar loginProgressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    TextView googleBtn;
    private GoogleSignInClient mGoogleSignInClient;

    // private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        login_username = findViewById(R.id.login_username);
        loin_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        loginProgressBar = findViewById(R.id.indeterminateBar);


        mAuth = FirebaseAuth.getInstance();


        // google sign in code
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBtn = findViewById(R.id.googleSignIn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIN();
            }
        });









        HomeActivity = new Intent(this, com.example.intoductionpart.MainActivityNavigationDrawer.class);

        loginProgressBar.setVisibility(View.INVISIBLE);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  loginProgressBar.setVisibility(View.VISIBLE);

                final String mail = login_username.getText().toString();
                final String password = loin_password.getText().toString();


                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify Fields");
                 //   loginProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    signIn(mail, password);
                }

            }
        });

    }

    private void signIN() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("SignIn class", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignIn class", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

//                           Intent i = new Intent(this,MainActivityNavigationDrawer.class);
//                           startActivity(i);

                          //  showMessage("Auth done");

                           updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                         ///   Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                          //  updateUI(null);

                              showMessage("Something wents wrong");

                        }

                        // ...
                    }
                });
    }

    private void signIn(String mail, String password) {


        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                  //  loginProgressBar.setVisibility(View.INVISIBLE);
                    updateUI();

                } else {
                    showMessage(task.getException().getMessage());

                  //  loginProgressBar.setVisibility(View.INVISIBLE);
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