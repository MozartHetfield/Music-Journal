package com.soare.musicjournal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AttendedConcerts extends Fragment implements View.OnClickListener {

    private RecyclerView concertRecyclerView;
    private static List<Concert> concerts;

    private View view;
    private Intent intent;
    private FragmentActivity fragmentActivity;
    private FloatingActionButton fab;

    private ConcertAdapter concertAdapter;

    private static Integer count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_attended_concerts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        setLayoutManager();

        if (concerts.isEmpty()) setConcertsFromDatabase();
        else {
            setAdapter();
            setRecyclerViewListener();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = getActivity();
        intent = fragmentActivity.getIntent();
    }

    private void initView() {
        concertRecyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.add_button);
        fab.setOnClickListener(this);

        if (concerts == null) concerts = new ArrayList<>();
    }

    public void setAdapter() {
        concertAdapter = new ConcertAdapter(concerts);
        concertRecyclerView.setAdapter(concertAdapter);
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity());
        concertRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {
        Intent newIntent = new Intent(getActivity(), ManageConcert.class);
        startActivity(newIntent);
    }

    public static List<Concert> getConcerts() {
        return concerts;
    }

    private void setImagesFromDatabase() {

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final long maxDimension = 1024 * 1024 * 10;

        for (final Concert concert : concerts) {
            StorageReference imageRef = storageRef.child(concert.getSingerName() + Const.getDateString((concert.getDate())));
            imageRef.getBytes(maxDimension)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            concert.setImage(bitmap);
                            synchronized (count) {
                                count++;
                            }
                            if (count == concerts.size()) {
                                setAdapter();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), getString(R.string.r_u_dumb), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void setConcertsFromDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("concerts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                concerts.add(document.toObject(Concert.class));
                            }
                            setImagesFromDatabase();
                            setAdapter();
                            setRecyclerViewListener();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.r_u_dumb), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setRecyclerViewListener() {
        concertRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                concertRecyclerView, new ConcertClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getActivity(), ViewConcert.class);
                intent.putExtra(Const.POSITION, position);
                intent.putExtra(Const.FAVOURITES, Const.ATTENDED);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ManageConcert.class);
                intent.putExtra(Const.POSITION, position);
                intent.putExtra(Const.EDIT, Const.EDIT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();

        setAdapter();
        setRecyclerViewListener();
    }
}
