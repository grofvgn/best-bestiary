package com.example.bestbestiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "sp_settings";

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
    Button btToMenu;
    Button btGameInfo;
    Button btGameSave;
    EditText txtName;

    List<Monster> monsters;
    JsonParser parser;
    int counter = 0;
    int ihd = 0;
    int ibd = 0;
    int ift = 0;

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
        btToMenu = (Button) findViewById(R.id.btnToMenu);
        btGameInfo = (Button) findViewById(R.id.btnGameInfo);
        btGameSave = (Button) findViewById(R.id.btnGameSave);
        txtName = (EditText) findViewById(R.id.txtGameName);

        txtName.setEnabled(false);
        txtName.setCursorVisible(false);

        btHeadBack.setOnClickListener(this);
        btHeadNext.setOnClickListener(this);
        btBodyBack.setOnClickListener(this);
        btBodyNext.setOnClickListener(this);
        btFootBack.setOnClickListener(this);
        btFootNext.setOnClickListener(this);
        btToMenu.setOnClickListener(this);
        btGameSave.setOnClickListener(this);
        btGameInfo.setOnClickListener(this);

        parser = new JsonParser();

        try {
            monsters = parser.deserialize(JsonParser.readGson(this, "gson.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        counter = monsters.size();
        checkMonsterName();
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
            case R.id.btnHeadBack:
                changeImage(imgHead, ihd, false, 0);
                checkMonsterName();
                break;
            case R.id.btnHeadNext:
                changeImage(imgHead, ihd, true, 0);
                checkMonsterName();
                break;
            case R.id.btnBodyBack:
                changeImage(imgBody, ibd, false, 1);
                checkMonsterName();
                break;
            case R.id.btnBodyNext:
                changeImage(imgBody, ibd, true, 1);
                checkMonsterName();
                break;
            case R.id.btnFootBack:
                changeImage(imgFoot, ift, false, 2);
                checkMonsterName();
                break;
            case R.id.btnFootNext:
                changeImage(imgFoot, ift, true, 2);
                checkMonsterName();
                break;
            case R.id.btnToMenu:
                backToMenu();
                break;
            case R.id.btnGameInfo:

                break;
            case R.id.btnGameSave:
                saveUserMonster();
                break;

        }
    }

    private void saveUserMonster() {
        txtName.setEnabled(true);
        txtName.setCursorVisible(true);
        txtName.setText("");
        if(txtName.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txtName, InputMethodManager.SHOW_IMPLICIT);
        }
        txtName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String myName = txtName.getText().toString();

                    List<Monster> myMonster = new ArrayList<Monster>();
                    // poskusimo dostopati do datoteke z lastnimi Posastmi
                    try {
                        myMonster = parser.deserialize(JsonParser.readGson(GameActivity.this, "myGson.txt"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // dodamo svojo posast na seznam
                    finally {
                        Monster m = new Monster(myName, monsters.get(ihd).getHeadName(), monsters.get(ibd).getBodyName(), monsters.get(ift).getFootName(), true);
                        myMonster.add(m);
                    }


                    try {
                        String json = parser.serialize(myMonster);
                        parser.writeGson(json, GameActivity.this, "myGson.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (GameActivity.this.getCurrentFocus() != null) {
                        InputMethodManager inputManager = (InputMethodManager) GameActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(GameActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    txtName.setEnabled(false);
                    txtName.setCursorVisible(false);
                    hideUI();
                    return true;
                }
                return false;
            }
        }
        );
    }

    private void checkMonsterName() {
        if (ihd == ibd && ihd == ift) {
            txtName.setText(monsters.get(ihd).getName());
            btGameInfo.setTextColor(this.getResources().getColor(R.color.colorWhite));
            btGameInfo.setEnabled(true);
        }
        else {
            txtName.setText("??????");
            btGameInfo.setTextColor(this.getResources().getColor(R.color.colorDarkRed));
            btGameInfo.setEnabled(false);
        }
    }

    private void backToMenu() {
        Intent intent = new Intent(GameActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeImage(ImageView img, int i, boolean next, int  type) {
        if (next) i++;
        else i--;
        if (i < 0) i = counter - 1;
        if (i > counter - 1) i = 0;
        String name;

        if (type == 0) {
            ihd = i;
            name = monsters.get(ihd).getHeadName();
        }
        else if (type == 1) {
            ibd = i;
            name = monsters.get(ibd).getBodyName();
        }
        else {
            ift = i;
            name = monsters.get(ift).getFootName();
        }

        int imageId = getResourseId(this, name, "drawable", getPackageName());
        img.setImageResource(imageId);
    }

    /*private void toGson(List<Monster> monsters) {
        try {
            String json = parser.serialize(monsters);
            parser.writeGson(json, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fromGson() {
        try {
            this.monsters = parser.deserialize(JsonParser.readGson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}
