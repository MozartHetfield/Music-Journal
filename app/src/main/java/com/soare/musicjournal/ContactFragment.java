package com.soare.musicjournal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;

public class ContactFragment extends Fragment implements View.OnClickListener {

    ImageView background;
    Intent svc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        background =  view.findViewById(R.id.contact_photo);
        background.setOnClickListener(this);
        svc = new Intent(getActivity(), BackgroundSoundService.class);
        getActivity().startService(svc);
        return view;
    }

    @Override
    public void onClick(View v) {
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=University+of+Bucharest");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().stopService(svc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(svc);
    }

}
