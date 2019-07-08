package com.example.android.erada2;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CenInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView InfoImage, InfoPhone, InfoEmail, InfoLocation;
    private TextView InfoName , InfoAddress  , InfoDesc , InfoWebsite ;
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Double lat , lag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cen_info);

        InfoImage = (ImageView) findViewById(R.id.cenInfo_image);
        InfoAddress = (TextView) findViewById(R.id.cenInfo_address);
       InfoPhone = (ImageView) findViewById(R.id.cenInfo_phone);
        InfoDesc = (TextView) findViewById(R.id.cenInfo_desc);
        InfoName = (TextView) findViewById(R.id.cenInfo_name);
        InfoWebsite = (TextView) findViewById(R.id.cenInfo_website);
        InfoEmail = (ImageView) findViewById(R.id.cenInfo_email);
       InfoLocation = (ImageView) findViewById(R.id.cenInfo_location);

       final Intent intent = getIntent();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.cenmapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(CenInfoActivity.this);



        lag = intent.getDoubleExtra("center_lag",0);
        lat =intent.getDoubleExtra("center_lat",0);
        InfoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lat != 0 && lag != 0) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lag);

                    Uri location = Uri.parse(uri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Toast.makeText(CenInfoActivity.this, "لايوجد موقع مضاف", Toast.LENGTH_SHORT).show();
                }
            }
        });


        InfoName.setText(intent.getStringExtra("center_name"));
        InfoAddress.setText(intent.getStringExtra("center_address"));
       // InfoPhone.setText(intent.getStringExtra("center_phone"));
        InfoWebsite.setText(intent.getStringExtra("center_website"));
        InfoDesc.setText(intent.getStringExtra("center_description"));
       // InfoEmail.setText(intent.getStringExtra("center_email"));
        Picasso.get().load(intent.getStringExtra("center_photo")).into(InfoImage);

        InfoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!intent.getStringExtra("center_phone").isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CenInfoActivity.this);

                builder.setTitle("الإتصال بالجهة");

                builder.setMessage("هل تريد الإتصال ب:" + intent.getStringExtra("center_name") + "\n" + intent.getStringExtra("center_phone"));
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri number = Uri.parse("tel:" + intent.getStringExtra("center_phone"));
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        PackageManager packageManager = getPackageManager();
                        List activities = packageManager.queryIntentActivities(callIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                        boolean isIntentSafe = ((List) activities).size() > 0;

                        if (isIntentSafe)
                            try {
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException e) {
                                String message = e.getMessage();
                                Toast.makeText(CenInfoActivity.this, "Error:" + message, Toast.LENGTH_LONG).show();
                            }
                    }
                });
                builder.setNegativeButton("إالغاء", null);
                builder.show();


            }else
                {
                    Toast.makeText(CenInfoActivity.this, "لايوجد رقم مضاف", Toast.LENGTH_LONG).show();

                }
            }
        });

        InfoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!intent.getStringExtra("center_email").isEmpty()){

                    Intent emailIntent = new Intent(intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + intent.getStringExtra("center_email")));
                    try {
                        startActivity(emailIntent);
                    } catch (ActivityNotFoundException e) {
                        String message = e.getMessage();
                        Toast.makeText(CenInfoActivity.this, "Error:" + message, Toast.LENGTH_LONG).show();
                    }
                }else
                {
                    Toast.makeText(CenInfoActivity.this,"لايوجد ايميل مضاف", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        LatLng ny = new LatLng(lat, lag);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gmap.addMarker(markerOptions);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));


    }
}
