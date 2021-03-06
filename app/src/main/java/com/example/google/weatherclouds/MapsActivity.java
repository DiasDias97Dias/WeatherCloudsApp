package com.example.google.weatherclouds;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private String mode;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        i=getIntent();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mode="default";
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
        if( i.getExtras()!=null && i.getStringExtra("x")!=null ){
            String msg=i.getStringExtra("msg");
            addTile("default");
            int x=Integer.valueOf(i.getStringExtra("x"));
            int y=Integer.valueOf(i.getStringExtra("y"));
            LatLng danger = new LatLng(x,y);
            int height = 50;
            int width = 50;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.icon);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            googleMap.addMarker(new MarkerOptions().position(danger)
                    .title(msg).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(danger));
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                addTile(mode);
            }
        });

    }

    private void addTile(String mode){
        if (mode.equals("default")){
            this.mode="temp";
            Toast.makeText(getApplicationContext(),"Temperature Map", Toast.LENGTH_SHORT).show();
            TileOverlay tileOverlayTemp = mMap.addTileOverlay(new TileOverlayOptions()
                    .tileProvider(tileTemp));
        } else if (mode.equals("temp")) {
            this.mode="clouds";
            Toast.makeText(getApplicationContext(),"Clouds Map", Toast.LENGTH_SHORT).show();
            TileOverlay tileOverlayClouds = mMap.addTileOverlay(new TileOverlayOptions()
                    .tileProvider(tileClouds));} else if (mode.equals("clouds")){
            this.mode="default";
            Toast.makeText(getApplicationContext(),"Default", Toast.LENGTH_SHORT).show();
        }
    }
}
