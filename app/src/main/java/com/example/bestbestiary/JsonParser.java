package com.example.bestbestiary;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;

public class JsonParser {

    final Gson gson = new Gson();

    public JsonParser() {
    }

    public String serialize(List<Monster> listMonster) throws IOException {

        String result = gson.toJson(listMonster);
        return result;
    }

    public List<Monster> deserialize(String json) {

        Type monsters = new TypeToken<List<Monster>>(){}.getType();
        List<Monster> listMonster = gson.fromJson(json, monsters);
        return listMonster;
    }

    public void writeGson(String json, Context ctx) throws IOException {

        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput("gson.txt", MODE_PRIVATE);
            fos.write(json.getBytes());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) fos.close();
        }

    }

    public static String readGson(Context ctx) throws IOException {

        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = ctx.openFileInput("gson.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String json;

            while ((json = br.readLine()) != null) {
                sb.append(json).append("");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                fis.close();
            }
        }
        return sb.toString();
    }


}
