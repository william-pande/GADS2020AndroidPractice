package ug.r.gadsleadershipmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import ug.r.gadsleadershipmobileapplication.views.InformationActivity;
import ug.r.gadsleadershipmobileapplication.views.LearningLeadersFragment;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, InformationActivity.class));
                finish();
            }
        }, 2000);
    }
}