package com.example.bestbestiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

public class LoadActivity extends BaseActivity implements View.OnClickListener{

    List<Monster> test;
    JsonParser parser;
    Monster loadedMonster;

    Button btMenu, btLoad, btRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        hideUI();
        parser = new JsonParser();

        btMenu = (Button) findViewById(R.id.btnBack);
        btLoad = (Button) findViewById(R.id.btnLoad2);
        btRemove = (Button) findViewById(R.id.btnRemove);

        btMenu.setOnClickListener(this);
        btLoad.setOnClickListener(this);
        btRemove.setOnClickListener(this);


        try {
                test = parser.deserialize(JsonParser.readGson(this, "myGson.txt"));
        } catch (IOException e) {
                e.printStackTrace();
        }

        if ( test != null && !test.isEmpty()) {
            loadedMonster = test.get(0);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
            DataAdapter.OnMonsterClickListener onUserClickListener = new DataAdapter.OnMonsterClickListener() {
                @Override
                public void onMonsterClick(Monster m) {
                    loadedMonster = m;
                }
            };
            DataAdapter adapter = new DataAdapter(this, test, onUserClickListener);
            recyclerView.setAdapter(adapter);
        }
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
            case R.id.btnBack:
                backToMenu();
                break;
            case R.id.btnLoad2:
                if (test != null) loadUserMonster();
                break;
            case R.id.btnRemove:
                if (test != null) removeUserMonster();
                break;
        }
    }

    private void removeUserMonster() {
        for (Monster m:
                test
             ) {
            if (m.equals(loadedMonster)) {
                test.remove(m);
                break;
            }
        }

        try {
            String json = parser.serialize(test);
            parser.writeGson(json, LoadActivity.this, "myGson.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoadActivity.this.recreate();
    }

    private void backToMenu() {
        Intent intent = new Intent(LoadActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserMonster() {
        Intent intent = new Intent(LoadActivity.this, GameActivity.class);
        intent.putExtra("loaded", true);
        intent.putExtra("head", loadedMonster.getHeadName());
        intent.putExtra("body", loadedMonster.getBodyName());
        intent.putExtra("foot", loadedMonster.getFootName());
        intent.putExtra("name", loadedMonster.getName());
        startActivity(intent);
        finish();
    }
}
