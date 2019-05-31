package com.soare.musicjournal;

import android.view.View;

public interface ConcertClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
