package com.example.bestbestiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "sp_settings";
    public static final String APP_PREFERENCES_HEAD = "Head";
    public static final String APP_PREFERENCES_BODY = "Body";
    public static final String APP_PREFERENCES_FOOT = "Foot";

    SharedPreferences spSettings;
    ImageView imgHead;
    ImageView imgBody;
    ImageView imgFoot;
    Button btHeadBack;
    Button btHeadNext;
    Button btBodyBack;
    Button btBodyNext;
    Button btFootBack;
    Button btFootNext;

    List<Monster> monsters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        spSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        imgHead = (ImageView) findViewById(R.id.imgGameHead);
        imgBody = (ImageView) findViewById(R.id.imgGameBody);
        imgFoot = (ImageView) findViewById(R.id.imgGameFoot);
        btHeadBack = (Button) findViewById(R.id.btnHeadBack);
        btHeadNext = (Button) findViewById(R.id.btnHeadNext);
        btBodyBack = (Button) findViewById(R.id.btnBodyBack);
        btBodyNext = (Button) findViewById(R.id.btnBodyNext);
        btFootBack = (Button) findViewById(R.id.btnFootBack);
        btFootNext = (Button) findViewById(R.id.btnFootNext);

        btHeadBack.setOnClickListener(this);
        btHeadNext.setOnClickListener(this);
        btBodyBack.setOnClickListener(this);
        btBodyNext.setOnClickListener(this);
        btFootBack.setOnClickListener(this);
        btFootNext.setOnClickListener(this);

        monsters = new ArrayList<Monster>();
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
        int imageId;
        switch (v.getId()) {
            case R.id.btnHeadBack:
                imageId = getResourseId(this, "bes_head", "drawable", getPackageName());
                imgHead.setImageResource(imageId);
                break;
            case R.id.btnHeadNext:
                imageId = getResourseId(this, "kikimora_head", "drawable", getPackageName());
                imgHead.setImageResource(imageId);
                break;
            case R.id.btnBodyBack:
                imageId = getResourseId(this, "bes_body", "drawable", getPackageName());
                imgBody.setImageResource(imageId);
                break;
            case R.id.btnBodyNext:
                imageId = getResourseId(this, "kikimora_body", "drawable", getPackageName());
                imgBody.setImageResource(imageId);
                break;
            case R.id.btnFootBack:
                break;
            case R.id.btnFootNext:
                break;
        }
    }

    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}
