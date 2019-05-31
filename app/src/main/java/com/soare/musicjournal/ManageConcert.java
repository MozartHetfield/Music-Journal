package com.soare.musicjournal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class ManageConcert extends AppCompatActivity {

    private TextView priceValueTextView;
    private ProgressBar priceProgressBar;
    private SeekBar priceSeekBar;
    private EditText singerNameEditText;
    private EditText locationEditText;
    private RadioGroup genreRadioGroup;
    private TextView dateEditText;
    private ImageView previewImageView;
    private RatingBar ratingRatingBar;

    private RadioButton rockRadioButton;
    private RadioButton popRadioButton;
    private RadioButton symphonicRadioButton;
    private RadioButton maneleRadioButton;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private static final String TAG = "ManageConcert";
    private Bitmap defaultPicture;

    private Concert concertToBeDeleted;
    private int positionOfConcertToBeDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_concert);

        initView();
        initializeDatePicker();
        initializeDefaultPicture();
        initializePriceValue();
        loadActivity();
    }

    private void initializePriceValue() {
        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceProgressBar.setProgress(progress);
                priceValueTextView.setText("" + progress + " RON");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initializeDatePicker() {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ManageConcert.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                dateEditText.setText(date);
            }
        };
    }

    public boolean verifyInput() {
        boolean booleanSinger = correctSingerName();
        boolean booleanLocation = correctLocationName();
        boolean booleanGenre = checkedGenre();
        boolean booleanDate = checkDate();

        return (booleanSinger && booleanLocation
                && booleanGenre && booleanDate);
    }

    public boolean correctSingerName() {
        if (singerNameEditText == null) return false;
        String name = singerNameEditText.getText().toString();
        if (name.length() < 3) {
            singerNameEditText.setError("Singer Name should contain at least 3 characters.");
            return false;
        } else if (name.length() > 25) {
            singerNameEditText.setError("Singer Name should contain maximum 25 characters.");
            return false;
        }
        singerNameEditText.setError(null);
        return true;
    }

    public boolean correctLocationName() {
        if (locationEditText == null) return false;
        String location = locationEditText.getText().toString();
        if (location.length() < 5) {
            locationEditText.setError("Location should contain at least 5 characters.");
            return false;
        } else if (location.length() > 35) {
            locationEditText.setError("Location should contain maximum 35 characters.");
            return false;
        }
        locationEditText.setError(null);
        return true;
    }

    public boolean checkDate() {
        if (dateEditText == null) return false;
        String date = dateEditText.getText().toString();
        if (date.length() == 0) {
            dateEditText.setError("Have you forgotten already?!");
            return false;
        }
        dateEditText.setError(null);
        return true;
    }

    public boolean checkedGenre() {
        if (genreRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Did they sing that bad?", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(maneleRadioButton);
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(symphonicRadioButton);
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(rockRadioButton);
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(popRadioButton);
            return false;
        }
        return true;
    }

    private void initView() {
        priceValueTextView = findViewById(R.id.priceValueTextView);
        priceProgressBar = findViewById(R.id.priceProgressBar);
        priceSeekBar = findViewById(R.id.priceSeekBar);
        singerNameEditText = findViewById(R.id.singerNameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        genreRadioGroup = findViewById(R.id.genreRadioGroup);
        previewImageView = findViewById(R.id.previewImageView);
        ratingRatingBar = findViewById(R.id.ratingBar);
        rockRadioButton = findViewById(R.id.rockRadioButton);
        popRadioButton = findViewById(R.id.popRadioButton);
        symphonicRadioButton = findViewById(R.id.symphonicRadioButton);
        maneleRadioButton = findViewById(R.id.maneleRadioButton);
    }

    public void selectFromGalleryOnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Const.REQUEST_GALLERY_ACCESS);
        }
    }

    private void initializeDefaultPicture() {
        if (previewImageView.getDrawable() == null) {
            defaultPicture = BitmapFactory.decodeResource(getResources(), R.drawable.default_pic);
            previewImageView.setImageBitmap(Const.compressBitmap(defaultPicture));
        }
        previewImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                previewImageView.setImageBitmap(Const.compressBitmap(defaultPicture));
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Const.REQUEST_IMAGE_CAPTURE) {
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                try {
                    previewImageView.setImageBitmap(capturedImage);
                } catch (Exception e) {
                    previewImageView.setImageBitmap(Const.compressBitmap(capturedImage));
                }
            } else if (requestCode == Const.REQUEST_GALLERY_ACCESS) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    try {
                        previewImageView.setImageBitmap(selectedImage);
                    } catch (Exception e) {
                        previewImageView.setImageBitmap(Const.compressBitmap(selectedImage));

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void makePhotoOnClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Const.REQUEST_IMAGE_CAPTURE);
        }
    }


    public void saveConcertOnClick(View view) {
        if (!verifyInput()) return;

        String singerName = singerNameEditText.getText().toString();
        String locationName = locationEditText.getText().toString();

        int genreId = genreRadioGroup.getCheckedRadioButtonId();
        RadioButton genreButton = findViewById(genreId);
        String genreName = genreButton.getText().toString();

        String priceValue = priceValueTextView.getText().toString();
        String dateValue = dateEditText.getText().toString();
        float ratingValue = ratingRatingBar.getRating();

        //previewImageView.invalidate();
        Bitmap previewImageBitmap = ((BitmapDrawable) previewImageView.getDrawable()).getBitmap();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (isEditConcert()) {
            imagesRef = storageRef.child(concertToBeDeleted.getSingerName() + Const.getDateString(concertToBeDeleted.getDate()));
            AttendedConcerts.getConcerts().remove(concertToBeDeleted);
            imagesRef.delete();
            db.collection("concerts").document(concertToBeDeleted.getSingerName() +
                    Const.getDateString(concertToBeDeleted.getDate())).delete();
        }

        final Concert concert = new Concert(singerName, locationName, genreName, priceValue, dateValue, ratingValue);
        concert.setImage(previewImageBitmap);

        imagesRef = storageRef.child(singerName + Const.getDateString(dateValue));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        previewImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data); //upload image

        db.collection("concerts").document(singerName + Const.getDateString(dateValue))
                .set(concert)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttendedConcerts.getConcerts().add(concert);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.fuck_android_studio_then),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isEditConcert() {
        Bundle dataReceived = getIntent().getExtras();
        if (dataReceived != null) {
            if (dataReceived.getString(Const.EDIT).equals(Const.EDIT)) {
                positionOfConcertToBeDeleted = dataReceived.getInt(Const.POSITION);
                concertToBeDeleted = AttendedConcerts.getConcerts().get(positionOfConcertToBeDeleted);
                getIntent().removeExtra(Const.EDIT);
                return true;
            }
        }
        return false;
    }

    public void loadActivity() {
        Bundle dataReceived = getIntent().getExtras();
        if (dataReceived != null) {
            int position = dataReceived.getInt(Const.POSITION);
            Concert concert = AttendedConcerts.getConcerts().get(position);

            singerNameEditText.setText(concert.getSingerName());
            locationEditText.setText(concert.getLocation());
            dateEditText.setText(concert.getDate());
            ratingRatingBar.setRating(concert.getRating());
            previewImageView.setImageBitmap(concert.getImage());

            String priceString = concert.getPrice();
            String[] priceStrings = priceString.split(" ");
            int price = Integer.parseInt(priceStrings[0]);
            priceSeekBar.setProgress(price);

            String genre = concert.getGenre();
            if (genre.equals(maneleRadioButton.getText().toString())) {
                maneleRadioButton.setChecked(true);
            } else if (genre.equals(rockRadioButton.getText().toString())) {
                rockRadioButton.setChecked(true);
            } else if (genre.equals(popRadioButton.getText().toString())) {
                popRadioButton.setChecked(true);
            } else if (genre.equals(symphonicRadioButton.getText().toString())) {
                symphonicRadioButton.setChecked(true);
            }
        }
    }
}
