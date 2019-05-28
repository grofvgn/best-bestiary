package com.example.bestbestiary;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class BaseActivity extends Activity {
    public static final String GAME_PREFERENCES = "GamePrefs";

    public void hideUI() {

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}