package com.soare.musicjournal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView username;
    private TextView gmail;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
        loadNavigationHeader();
    }

    private void loadNavigationHeader() {
        Bundle dataReceived = getIntent().getExtras();
        if (dataReceived != null) {
            username.setText(dataReceived.getString(Const.NAME));
            gmail.setText(dataReceived.getString(Const.GMAIL));

            String imageString = dataReceived.getString(Const.PHOTO);
            Uri imageUrl = Uri.parse(imageString);
            Picasso.get().load(imageUrl).into(image);
        }
    }

    private void initView(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.account_name);
        gmail = headerView.findViewById(R.id.account_gmail);
        image = headerView.findViewById(R.id.account_image);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AttendedConcerts()).commit();
            navigationView.setCheckedItem(R.id.homeIcon);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.homeIcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AttendedConcerts()).commit();
                break;
            case R.id.favouritesIcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavouritesFragment()).commit();
                break;
            case R.id.aboutIcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InfoFragment()).commit();
                break;
            case R.id.contactIcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactFragment()).commit();
                break;
            case R.id.sendIcon:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("plain/text");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android Fundamentals 2019");
                intent.putExtra(Intent.EXTRA_TEXT, "It worked! :)");
                startActivity(Intent.createChooser(intent, ""));
                break;
            case R.id.shareIcon:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Check out my first Android Studio project: https://github.com/MozartHetfield/Music-Journal");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
