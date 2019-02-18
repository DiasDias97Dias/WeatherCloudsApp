package com.example.google.weatherclouds;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {
    private TileProvider tileTemp;
    private TileProvider tileClouds;
    private GoogleMap mMap;
    private int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mode=0;
        // Move the camera
        LatLng astana = new LatLng(51, 71);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(astana, 2));

        tileTemp = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                /* Define the URL pattern for the tile images */
                String s = String.format( "http://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=246e0838dbbffd086e19c8cae2dec946",
                        zoom, x, y);

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

        };
        tileClouds = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                /* Define the URL pattern for the tile images */
                String s = String.format( "http://tile.openweathermap.org/map/clouds_new/%d/%d/%d.png?appid=246e0838dbbffd086e19c8cae2dec946",
                        zoom, x, y);

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

        };
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mode==10) mode=2;
                mode++;
                mMap.clear();
                if (mode%3==1){
                    Toast.makeText(getApplicationContext(),"Temperature Map", Toast.LENGTH_SHORT).show();
                    TileOverlay tileOverlayTemp = mMap.addTileOverlay(new TileOverlayOptions()
                            .tileProvider(tileTemp));
                } else if (mode%3==2) {
                    Toast.makeText(getApplicationContext(),"Clouds Map", Toast.LENGTH_SHORT).show();
                    TileOverlay tileOverlayClouds = mMap.addTileOverlay(new TileOverlayOptions()
                            .tileProvider(tileClouds));} else {
                    Toast.makeText(getApplicationContext(),"Default", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
