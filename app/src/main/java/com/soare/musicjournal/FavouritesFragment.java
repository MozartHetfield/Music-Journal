package com.soare.musicjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment{

    private RecyclerView concertRecyclerView;

    private static List<Concert> concerts;

    View view;
    Intent intent;
    FragmentActivity fragmentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favourites_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        setLayoutManager();
        setAdapter();
        setRecyclerViewListener();
        updateConcerts();
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        concertRecyclerView.setLayoutManager(layoutManager);
    }

    private void updateConcerts() {
        List<Concert> allConcerts = AttendedConcerts.getConcerts();
        for (Concert currentConcert : allConcerts) {
            if (currentConcert.getRating() < 5) continue;
            concerts.add(currentConcert);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = getActivity();
        intent = fragmentActivity.getIntent();
    }

    private void initView() {
        concertRecyclerView = view.findViewById(R.id.recyclerViewFavourites);
        concerts = new ArrayList<>();
    }

    private void setAdapter() {
        ConcertAdapter concertAdapter = new ConcertAdapter(concerts);
        concertRecyclerView.setAdapter(concertAdapter);
    }

    public static List<Concert> getConcerts() {
        return concerts;
    }

    private void setRecyclerViewListener() {
        concertRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                concertRecyclerView, new ConcertClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getActivity(), ViewConcert.class);
                intent.putExtra(Const.POSITION, position);
                intent.putExtra(Const.FAVOURITES, Const.FAVOURITES);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getActivity(), getString(R.string.price_to_pay), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), getString(R.string.nope), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
