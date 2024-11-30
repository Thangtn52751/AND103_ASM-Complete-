package dev.md19303.and103_asmcompleted;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {
    SharedPreferences preferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String FIRST_TIME_KEY = "firstTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(FIRST_TIME_KEY, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isFirstTime) {
                    intent = new Intent(Splash_Screen.this, Introduction_Activity.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(FIRST_TIME_KEY, false);
                    editor.apply();
                } else {
                    intent = new Intent(Splash_Screen.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);


    }
}