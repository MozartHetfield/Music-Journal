package com.soare.musicjournal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ConcertViewHolder extends RecyclerView.ViewHolder {

    private TextView singerName;
    private TextView location;
    private TextView genre;
    private TextView price;
    private TextView date;
    private RatingBar rating;
    private ImageView image;

    public ConcertViewHolder(@NonNull View itemView) {
        super(itemView);

        singerName = itemView.findViewById(R.id.singerNameCardView);
        location = itemView.findViewById(R.id.locationCardView);
        genre = itemView.findViewById(R.id.genreCardView);
        price = itemView.findViewById(R.id.priceCardView);
        date = itemView.findViewById(R.id.dateCardView);
        rating = itemView.findViewById(R.id.ratingCardView);
        image = itemView.findViewById(R.id.imageCardView);
    }

    public TextView getSingerName() {
        return singerName;
    }

    public TextView getLocation() {
        return location;
    }

    public TextView getGenre() {
        return genre;
    }

    public TextView getPrice() {
        return price;
    }

    public TextView getDate() {
        return date;
    }

    public RatingBar getRating() {
        return rating;
    }

    public ImageView getImage() {
        return image;
    }

}
