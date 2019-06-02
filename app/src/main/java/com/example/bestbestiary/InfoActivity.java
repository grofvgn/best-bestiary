package com.example.bestbestiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    TextView txtName, txtDescription;
    Button btGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        hideUI();

        txtName = (TextView) findViewById(R.id.txtInfoName);
        txtDescription = (TextView) findViewById(R.id.txtInfoDescription);
        txtDescription.setMovementMethod(new ScrollingMovementMethod());
        btGame = (Button) findViewById(R.id.btnInfoBack);
        btGame.setOnClickListener(this);

        Intent intent = getIntent();
        String monsterName = intent.getStringExtra("infoName");
        String monsterDescription = intent.getStringExtra("infoDescription");

        txtName.setText(monsterName);
        txtDescription.setText(monsterDescription);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideUI();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInfoBack:
                Intent intent = new Intent(InfoActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void backToGame() {

    }
}
