package com.soare.musicjournal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Const {
    public static final int REQUEST_IMAGE_CAPTURE = 42;
    public static final int REQUEST_GALLERY_ACCESS = 77;
    public static final int GOOGLE_SIGN_IN = 101;

    public static final String GMAIL = "GMAIL";
    public static final String NAME = "NAME";
    public static final String PHOTO = "PHOTO";

    public static final String POSITION = "POSITION";
    public static final String EDIT = "EDIT";
    public static final String FAVOURITES = "FAVOURITES";
    public static final String ATTENDED = "ATTENDED";

    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byte[] byteArray = stream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }

    public static String getDateString(String date) {
        String result = "";
        String parts[] = date.split("/");
        for (int i = 0; i < parts.length; i++) {
            result += parts[i];
        }
        return result;
    }

}
