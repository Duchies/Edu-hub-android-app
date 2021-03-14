package com.example.intoductionpart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityNavigationDrawer extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    add_to_cart add_to_cart_badge;
    FirebaseUser currentUser ;
    FirebaseAuth mAuth;

    add_to_cart obj;
    TextView textCartItemCount,viewAll1,viewAll2,viewAll3;
    int mCartItemCount = 0;
    private DrawerLayout drawer;
    SwipeRefreshLayout srl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        srl = findViewById(R.id.swipeRefreshLayout_in_main);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                datachange();
//            }
//        });
//

        // add_to_cart_badge.datachange();


        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent ite = new Intent(getApplicationContext(),item_display_place.class);
//                startActivity(ite);
//
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);









        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavHeader();


        //Image slider part

        ImageSlider imageSlider  = (ImageSlider) findViewById(R.id.slider);
        List<SlideModel> slideModels  = new ArrayList<>();

        slideModels.add(new SlideModel("https://raw.githubusercontent.com/Duchies/PhotoResources/master/banner2.png", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://raw.githubusercontent.com/Duchies/PhotoResources/master/banner1.png",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://raw.githubusercontent.com/Duchies/PhotoResources/master/banner6.jpg",ScaleTypes.FIT));
//        slideModels.add(new SlideModel("https://rukminim1.flixcart.com/image/880/1056/jp5sknk0/backpack/3/x/e/hp0008-ezhp0008-laptop-backpack-hp-original-imafbgmyv4ymwbkb.jpeg?q=50","title 4",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://raw.githubusercontent.com/Duchies/PhotoResources/master/banner5.jpg",ScaleTypes.FIT));



        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        imageSlider.startSliding(5000) ;// with new period
        imageSlider.startSliding(5000);
        imageSlider.stopSliding();

        // end image slider part



        viewAll1 = findViewById(R.id.viewAll1);
        viewAll2 = findViewById(R.id.viewAll2);
        viewAll3 = findViewById(R.id.viewAll3);

        viewAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPlaceGoMethod();
            }
        });

        viewAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPlaceGoMethod();
            }
        });

        viewAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPlaceGoMethod();
            }
        });


        datachange();

    }



    // text on click below is same

    private void displayPlaceGoMethod() {

        Intent intent = new Intent(this,item_display_place.class);
        startActivity(intent);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_navigation_drawer, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_userMail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_imageView);



        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);



    }

    public void log_out_nav_operation(MenuItem item) {

        FirebaseAuth.getInstance().signOut();
        Intent out_to_login = new Intent(getApplicationContext(),Sign_up.class);
        startActivity(out_to_login);
        finish();

    }




    public void ContactUs(MenuItem item) {
        Intent i=new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Reason For Contact");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "text that you want to put");
        startActivity(Intent.createChooser(i,"Contact via"));

    }
    public void UserDetailMethod(MenuItem item) {

        Intent intent  = new Intent(this,UserDetailsPage.class);
        startActivity(intent);

    }

//
//    public void viewAll() {
//        Intent intent = new Intent(this,item_display_place.class);
//        startActivity(intent);
//    }

    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void datachange() {


        FirebaseDatabase.getInstance().getReference().child("login_detail")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String toaddOverdatabaseList = snapshot.child("idDatabase").getValue().toString();
                            String str = snapshot.child("price").getValue().toString();
                            mCartItemCount = mCartItemCount + 1;
                        }

                        setupBadge();
                        mCartItemCount = 0;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}