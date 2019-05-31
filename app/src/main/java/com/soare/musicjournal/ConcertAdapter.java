package com.soare.musicjournal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ConcertAdapter extends RecyclerView.Adapter<ConcertViewHolder> {

    private static List<Concert> concerts;

    public ConcertAdapter(List<Concert> concerts) {
        ConcertAdapter.concerts = concerts;
    }

    @NonNull
    @Override
    public ConcertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;

        itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.concert_card_view,
                        viewGroup, false);

        return new ConcertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConcertViewHolder concertViewHolder, int i) {
        Concert currentConcert = concerts.get(i);
        if (currentConcert != null) {
            if (currentConcert.getSingerName() != null) {
                concertViewHolder.getSingerName().setText(currentConcert.getSingerName());
            }
            if (currentConcert.getLocation() != null) {
                concertViewHolder.getLocation().setText(currentConcert.getLocation());
            }
            if (currentConcert.getGenre() != null) {
                concertViewHolder.getGenre().setText(currentConcert.getGenre());
            }
            if (currentConcert.getPrice() != null) {
                concertViewHolder.getPrice().setText(currentConcert.getPrice());
            }
            if (currentConcert.getDate() != null) {
                concertViewHolder.getDate().setText(currentConcert.getDate());
            }
            if (currentConcert.getRating() >= 0 && currentConcert.getRating() <= 5) {
                concertViewHolder.getRating().setRating(currentConcert.getRating());
            }
            if (currentConcert.getImage() != null) {
                concertViewHolder.getImage().setImageBitmap(currentConcert.getImage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return concerts.size();
    }
}
