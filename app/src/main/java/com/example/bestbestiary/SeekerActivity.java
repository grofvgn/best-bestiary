package com.example.bestbestiary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class SeekerActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "SeekerActivity";
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int PERMISSION_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermission = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFLPC;

    EditText txtSearch;
    ImageView imgMyLoc;

    List<Monster> monsters;
    JsonParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);
        parser = new JsonParser();

        try {
            monsters = parser.deserialize(JsonParser.readGson(this, "gson.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtSearch = (EditText) findViewById(R.id.txtSeekerSearch);
        imgMyLoc = (ImageView) findViewById(R.id.imgMyLocatio);
        hideUI();

        getLocationPermission();
    }
    private void init(){
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
            }
        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if(actionId == EditorInfo.IME_ACTION_SEARCH
               || actionId == EditorInfo.IME_ACTION_DONE
               || event.getAction() == KeyEvent.ACTION_DOWN
               || event.getAction() == KeyEvent.KEYCODE_ENTER){

                   //iscemo lokacijo
                   geoLocate();
               }

                return false;
            }
        });
        hideKeyboard();
        imgMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
    }

    private void geoLocate() {
        // TODO: listAdres zamenjati z monsters
        // TODO: ponujati na izbiro imena monstrov, nato naloziti njihovo lokacijo LatLng
        String strSearch = txtSearch.getText().toString();
        Monster sMonster = null;
        if (monsters.size() > 0) {
            for (Monster m:
                    monsters
                 ) {
                if (strSearch.equals(m.getName())) {
                    sMonster = m;
                    break;
                }
            }
            if(sMonster != null) moveCamera(sMonster.getMonsterLocation(), DEFAULT_ZOOM, sMonster.getName());
            else Toast.makeText(this, "Unable to find this monster...", Toast.LENGTH_SHORT).show();
        }
        /*Geocoder geocoder = new Geocoder(SeekerActivity.this);
        List<Address> listAddresses = new ArrayList<>();
        try{
            listAddresses = geocoder.getFromLocationName(strSearch, 1);
        }
        catch (IOException e){
            Log.e("TAG", "IOExc: " + e.getMessage());
        }

        if (listAddresses.size() > 0) {
            Address a = listAddresses.get(0);

            moveCamera(new LatLng(a.getLatitude(), a.getLongitude()),
                    DEFAULT_ZOOM, a.getAddressLine(0));
        } */
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermission = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
        }
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermission = false;

        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermission = false;
                            return;
                        }
                    }
                    mLocationPermission = true;
                    //init map
                    initMap();
                }
        }
    }

    private void getMyLocation() {
        Log.d(TAG, "INIT MAP");
        mFLPC = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermission) {
                final Task location = mFLPC.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location was founded!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
                        } else {
                            Toast.makeText(SeekerActivity.this, "Unable to found current location!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "Moving gamera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) { // dodamo ime Marker-ja
            MarkerOptions marker = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(marker);
        }
        hideKeyboard();
    }

    private void hideKeyboard() {
        if (SeekerActivity.this.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) SeekerActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(SeekerActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        txtSearch.setCursorVisible(false);
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(SeekerActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermission) {
            getMyLocation(); // dobimo lokacijo
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);

            init();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideUI();
        }
    }
}
