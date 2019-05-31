package com.soare.musicjournal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginScreen extends AppCompatActivity {

    private CheckBox termsAndConditions;
    private SignInButton submitButton;
    private ConstraintLayout background;

    private static final String TAG = "LoginScreen";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initView();
        createBackgroundAnimation();
        setGoogleButton();
    }

    private void customizeSubmitButton() {
        int count = submitButton.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = submitButton.getChildAt(i);
            if (v instanceof TextView) {
                Spannable wordtoSpan = new SpannableString("Create your journey!");
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(66, 133, 244)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(219, 68, 55)), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(244, 160, 0)), 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(66, 133, 244)), 10, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(15, 157, 88)), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(219, 68, 55)), 17, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) v).setText(wordtoSpan);
                return;
            }
        }
    }

    private void setGoogleButton() {
        customizeSubmitButton();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyInput()) return;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    private void createBackgroundAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) background.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    private void initView() {
        termsAndConditions = findViewById(R.id.termsAndConditions);
        submitButton = findViewById(R.id.submitButton);
        background = findViewById(R.id.backgroundLoginScreen);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goHomeActivityIfAuthenticated();
                        } else {
                            Log.w("login", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void goHomeActivityIfAuthenticated() {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);

            intent.putExtra(Const.GMAIL, mAuth.getCurrentUser().getEmail());
            intent.putExtra(Const.NAME, mAuth.getCurrentUser().getDisplayName());
            intent.putExtra(Const.PHOTO, mAuth.getCurrentUser().getPhotoUrl().toString());

            startActivity(intent);
            finish();
        }
    }

    public boolean verifyInput() {
        return isChecked();
    }

    public boolean isChecked() {
        if (termsAndConditions.isChecked()) return true;
        Toast.makeText(getApplicationContext(), getString(R.string.love_android_studio), Toast.LENGTH_SHORT).show();
        return false;
    }
}
