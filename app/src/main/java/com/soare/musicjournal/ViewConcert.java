package com.soare.musicjournal;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class ViewConcert extends AppCompatActivity implements View.OnClickListener {

    private TextView singerName;
    private TextView location;
    private TextView genre;
    private TextView price;
    private TextView date;
    private RatingBar rating;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_concert);

        initView();
        setView();
    }

    private void setView() {
        Bundle dataReceived = getIntent().getExtras();
        if (dataReceived != null) {

            int position = dataReceived.getInt(Const.POSITION);
            String checkFavourites = dataReceived.getString(Const.FAVOURITES);
            Concert concert = null;
            if (checkFavourites == null || !checkFavourites.equals(Const.FAVOURITES)) {
                concert = AttendedConcerts.getConcerts().get(position);
            } else {
                concert = FavouritesFragment.getConcerts().get(position);
            }

            singerName.setText(concert.getSingerName());
            location.setText(concert.getLocation());
            genre.setText(concert.getGenre());
            price.setText(concert.getPrice());
            date.setText(concert.getDate());
            rating.setRating(concert.getRating());
            image.setImageBitmap(concert.getImage());
        }
    }

    private void initView() {
        singerName = findViewById(R.id.singerNameViewConcert);
        location = findViewById(R.id.location_preview);
        genre = findViewById(R.id.genre_view_concert);
        price = findViewById(R.id.priceViewConcert);
        date = findViewById(R.id.date_view_concert);
        rating = findViewById(R.id.rating_view_concert);
        image = findViewById(R.id.previewViewConcert);

        ConstraintLayout background = findViewById(R.id.clickableBackground);
        background.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Bundle dataReceived = getIntent().getExtras();
        String checkFavourites = dataReceived.getString(Const.FAVOURITES);
        if (checkFavourites.equals(Const.ATTENDED)) {
            int deleteRandom = new Random().nextInt(10) + 1;
            if (deleteRandom == 7) {
                int position = dataReceived.getInt(Const.POSITION);

                Concert concert = AttendedConcerts.getConcerts().get(position);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imagesRef = null;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                imagesRef = storageRef.child(concert.getSingerName() + Const.getDateString(concert.getDate()));
                AttendedConcerts.getConcerts().remove(concert);
                imagesRef.delete();
                db.collection("concerts").document(concert.getSingerName() +
                        Const.getDateString(concert.getDate())).delete();
                Toast.makeText(ViewConcert.this, "You wished for it!",
                        Toast.LENGTH_LONG).show();

                finish();
            } else
                Toast.makeText(ViewConcert.this, deleteRandom + " out of 10. Lucky!",
                        Toast.LENGTH_LONG).show();
        }

        int firstRandom = new Random().nextInt(7);
        Techniques[] techniques = Techniques.values();
        int secondRandom = new Random().nextInt(techniques.length);

        switch (firstRandom) {
            case 0:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(singerName);
                break;
            case 1:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(location);
                break;
            case 2:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(genre);
                break;
            case 3:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(image);
                break;
            case 4:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(rating);
                break;
            case 5:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(price);
                break;
            case 6:
                YoYo.with(techniques[secondRandom]).duration(700).repeat(1).playOn(date);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}

