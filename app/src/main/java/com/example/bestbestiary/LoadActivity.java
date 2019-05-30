package com.example.bestbestiary;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class LoadActivity extends BaseActivity{

    TextView loaded;
    List<Monster> test;
    JsonParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        loaded = (TextView) findViewById(R.id.txtLoadedMonster);
        parser = new JsonParser();

        try {
            test = parser.deserialize(JsonParser.readGson(this, "myGson.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = "";

        for (Monster m:
                test
             ) {
            res += m.getName() + " ";
        }

        loaded.setText(res);
    }
}
