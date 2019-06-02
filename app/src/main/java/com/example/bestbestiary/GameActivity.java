package com.example.bestbestiary;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements View.OnClickListener {

    public static final String APP_PREFERENCES = "sp_settings";
    private static final String TAG = "GameActivity";
    private static final int ERROR_REQUEST = 9001;
    // API KEY: AIzaSyBqNiIEPAg1rWyQn6oxdP5YmErIjMGgK9o

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
    Button btMap;
    EditText txtName;

    List<Monster> monsters;
    JsonParser parser;
    int counter = 0;
    int ihd = 0;
    int ibd = 0;
    int ift = 0;

    Intent intent;
    String intHead;
    String intBody;
    String intFoot;

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
        btMap = (Button) findViewById(R.id.btnGameHunt);
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
        btMap.setOnClickListener(this);


        parser = new JsonParser();
        intent = getIntent(); // dobimo podatki iz LoadActivity
        intHead = intent.getStringExtra("head");
        intBody = intent.getStringExtra("body");
        intFoot = intent.getStringExtra("foot");

        initData();
        setLoaded();
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
                toInfo();
                break;
            case R.id.btnGameSave:
                saveUserMonster();
                break;
            case R.id.btnGameHunt:
                if(isServiceWork()) {
                    initMap();
                }
                break;

        }
    }

    private void initMap() {
        Intent intent = new Intent(GameActivity.this, SeekerActivity.class);
        startActivity(intent);
    }

    public void initData() {
        try { // ce obstaja datoteka gson.txt, potem nalozimo podatki, drugace inicializiramo zacetni podatki
            if(fileExist("gson.txt")) monsters = parser.deserialize(JsonParser.readGson(this, "gson.txt"));
            else {
                monsters = new ArrayList<Monster>();
                // TODO : zanka za dodajanje
                monsters.add(new Monster("Kikimora", "kikimora_head", "kikimora_body", "kikimora_foot", true,
                        getResourseId(GameActivity.this, "kikimora_head", "drawable", getPackageName()), "Kikimora is a legendary creature, a female house spirit in Slavic mythology. Her role in the house is usually juxtaposed with that of the domovoy, whereas one of them is considered a bad spirit, and the other, a \"good\" one. When the kikimora inhabits a house, she lives behind the stove or in the cellar, and usually produces noises similar to those made by the mice in order to obtain food. Kikimory (in plural) were the first traditional explanation for sleep paralysis in Russian folklore.\n" +
                        "\n" +
                        "The word kikimora may have derived from Udmurt (Finn-Ugric) word kikka-murt, meaning scarecrow (literally bag-made person), although other etymological hypotheses also exist. The OED links mora with the mare of nightmare; moreover, inconclusive linguistic evidence suggests that the French word cauchemar might have also derived from the same root.",
                        new LatLng(46.559090, 15.638078)));

                monsters.add(new Monster("Bes", "bes_head", "bes_body", "bes_foot", true,
                        getResourseId(GameActivity.this, "bes_head", "drawable", getPackageName()),"Bes is an evil spirit or demon in Slavic mythology. The word is synonymous with chort.\n" +
                        "\n" +
                        "After the acceptance of Christianity the bies became identified with the devil, corresponding to the being referred to in Ancient Greek, as either daimon (δαίμων), daimónion or pneuma (πνεῦμα). For example, biesy (Russian plural of bies) is used in the standard Russian translation of Mark 5:12, where we have the devils entering the swine in KJV. Compare to the Ukrainian bisy (used always in plural) or bisytysia (to go mad). In Slovenian (bes), Croatian (bijes) and Serbian (bes) the word means \"rage\", \"fury\".",
                        new LatLng(46.559090, 15.638078)));
                try { // po inicializaciji shranimo v gson.txt
                    parser.writeGson(parser.serialize(monsters), GameActivity.this, "gson.txt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoaded() {
        if (intent.hasExtra("loaded")) {// ce imamo podatki iz Load, potem nastavimo slike
            imgHead.setImageResource(getResourseId(this, intent.getStringExtra("head"), "drawable",
                    getPackageName()));
            imgBody.setImageResource(getResourseId(this, intent.getStringExtra("body"), "drawable",
                    getPackageName()));
            imgFoot.setImageResource(getResourseId(this, intent.getStringExtra("foot"), "drawable",
                    getPackageName()));
            txtName.setText(intent.getStringExtra("name"));
            // v zanki nastavimo stevce za posamezno sliko
            for (Monster m:
                    monsters
            ) {
                if (intHead.equals(m.getHeadName())){
                    ihd = monsters.indexOf(m);
                }
                if ((intBody.equals(m.getBodyName()))) {
                    ibd = monsters.indexOf(m);
                }
                if (intFoot.equals(m.getFootName())) {
                    ift = monsters.indexOf(m);
                }
            }
        }
        else checkMonsterName(); // ce ne nalagamo monstra, nastavimo njegovo ime glede na indeks
        counter = monsters.size(); // nastavimo stevec velikosti seznama monster
    }

    public boolean fileExist(String path) {
        File file = getBaseContext().getFileStreamPath(path);
        return file.exists();
    }

    public boolean isServiceWork() {
        Log.d(TAG, "Checking version...");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(GameActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            // OK user can make request
            Log.d(TAG, "Everything is working!");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error we can resolve
            Log.d(TAG, "Something wrong, but we can resolve this...");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(GameActivity.this, available, ERROR_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can't make map request...", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                       if (fileExist("myGson.txt")) myMonster = parser.deserialize(JsonParser.readGson(GameActivity.this, "myGson.txt"));
                        Monster m = new Monster(myName, monsters.get(ihd).getHeadName(), monsters.get(ibd).getBodyName(), monsters.get(ift).getFootName(), true,
                                getResourseId(GameActivity.this, monsters.get(ihd).getHeadName(), "drawable", getPackageName()), "", null);
                        myMonster.add(m);
                    } catch (IOException e) {
                        e.printStackTrace();
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

    private void toInfo() {
        Monster monster = null;
        for (Monster m:
             monsters) {
            if (m.getName().equals(txtName.getText().toString())) {
                monster = m;
                break;
            }
        }
        Intent intent = new Intent(GameActivity.this, InfoActivity.class);
        intent.putExtra("infoName", txtName.getText().toString());
        intent.putExtra("infoDescription", monster.getDescription().toString());
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


}
