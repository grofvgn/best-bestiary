package com.example.bestbestiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicializacija Button-ov
        Button btnNew = (Button) findViewById(R.id.btnMenuNew);
        Button btnLoad = (Button) findViewById(R.id.btnMenuLoad);
        Button btnSettings = (Button) findViewById(R.id.btnMenuSettings);
        Button btnHelp = (Button) findViewById(R.id.btnMenuHelp);
        Button btnExit = (Button) findViewById(R.id.btnMenuExit);

        // Nastavitev listener-a
        btnNew.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnMenuNew:
                startNewMonster();
                break;
            case R.id.btnMenuLoad:
                startLoadMonster();
                break;
            case R.id.btnMenuSettings:
                startSettings();
                break;
            case R.id.btnMenuHelp:
                startHelp();
                break;
            case R.id.btnMenuExit:
                closeApp();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideUI();
        }
    }

    // Zapiranje aplikacije
    private void closeApp() {
        finish();
        System.exit(0);
    }

    // Odpiranje GameActivity
    private void startNewMonster() {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    // Odpiranje LoadActivity
    private void startLoadMonster() {
        Intent intent = new Intent(MenuActivity.this, LoadActivity.class);
        startActivity(intent);
        finish();
    }

    // Odpiranje SettingsActivity
    private void startSettings() {
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    // Odpiranje HelpActivity
    private void startHelp() {
        Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
        startActivity(intent);
        finish();
    }
}
