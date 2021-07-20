package com.example.forexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;




public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView img;
    TextView name, email;
    Button Logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        img= findViewById(R.id.userImg);
        name= findViewById((R.id.dashboardEmail));
        email= findViewById((R.id.dashboardName));
        Logoutbtn= findViewById(R.id.LogoutBtn);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        assert account != null;
        name.setText(account.getDisplayName());
        email.setText(account.getEmail());
        Glide.with(this).load(account.getPhotoUrl()).into(img);


        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });



        getSupportActionBar();


        /*---------------------Hooks------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.obey_home:
                        break;
                    case R.id.obey_profile:
                        Intent intent = new Intent(Dashboard.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.obey_share:
                        Toast.makeText(Dashboard.this, "Share this with your loved ones", Toast.LENGTH_SHORT).show();
                        break;

                }

                //there is false Earlier
                return false;
            }
        });
        /*--------------------toolbar-----------------*/

        /*-------------------Navigation Drawer menu-----------------*/
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
