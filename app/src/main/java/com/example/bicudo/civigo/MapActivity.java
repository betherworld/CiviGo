package com.example.bicudo.civigo;

import com.example.bicudo.civigo.map.PositionItemizedOverlay;
import com.example.bicudo.civigo.map.NewPoiActivity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import android.os.Environment;
import android.util.Log;

import java.util.List;


public class MapActivity extends AppCompatActivity implements LocationListener {

    private MapView myOpenMapView;
    private IMapController myMapController;
    private LocationManager locationManager;
    private Location currentLocation;

    private static final int REQUEST_PERMISSIONS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // A bit annoying permission checks, enforced by newer Android versions. You do not
        // have to understand much, except that this lets a popup appear, which asks the
        // user to give permission to write to the SD card.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        }


        // create osmdroid view (map)
        myOpenMapView = (MapView) findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        // The controller allows us to zoom, pan, etc. on the map.
        myMapController = myOpenMapView.getController();
        myMapController.setZoom(16);

        // set default location
        currentLocation = new Location("");
        currentLocation.setLatitude(47.408632d);
        currentLocation.setLongitude(8.50713d);

        // add onclick listeners for buttons
        Button addPoiButton = (Button) findViewById(R.id.ButtonAddPoi);
        addPoiButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickAddPoi();
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Again, some permission checks, in this case for location access.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }

        // We request the location updates every 1000 milliseconds, or
        // every 5 meters traveled by the user.
        // This is done in onStart(...), so that the location updates are enabled whenever the
        // application gets into focus again.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Here we stop the location manager updates, so that the tracking doesn't run in the
        // background, when the app is not activated.
        locationManager.removeUpdates(this);
    }


    private void updatePoiOverlays() {


        // overlay item aktualisiseren
        Drawable drawable = this.getResources().getDrawable(R.drawable.face_glasses);
        PositionItemizedOverlay itemizedoverlay = new PositionItemizedOverlay(drawable, this);
        GeoPoint itempoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        OverlayItem overlayitem = new OverlayItem("GIS II", "Actual position", itempoint);
        itemizedoverlay.addOverlay(overlayitem);

        // reload overlays
        myOpenMapView.getOverlays().clear();
        myOpenMapView.getOverlays().add(itemizedoverlay);
        myMapController.setCenter(itempoint);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File directory = Environment.getExternalStorageDirectory();
                File f = new File(directory, "pois.csv");
                FileReader fileReader = new FileReader(f);
                BufferedReader reader = new BufferedReader(fileReader);
                String lineRead = reader.readLine();
                while (lineRead != null) {
                    String[] columns = lineRead.split(";");
                    double longitude = Double.parseDouble(columns[0]);
                    double latitude = Double.parseDouble(columns[1]);
                    Location poiLocation = new Location("");
                    poiLocation.setLongitude(longitude);
                    poiLocation.setLatitude(latitude);
                    float[] results = new float[3];
                    Location.distanceBetween(poiLocation.getLatitude(), poiLocation.getLongitude(),
                            currentLocation.getLatitude(), currentLocation.getLongitude(), results);
                    float distance = results[0];
                    if (distance < 100) {
                        addPOIOverlay(poiLocation);
                    }
                    lineRead = reader.readLine();
                }
                reader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("FileLog", "SD card not mounted");
        }

    }

    private void OnClickAddPoi() {
        Intent newPoiIntent = new Intent(this, NewPoiActivity.class);
        newPoiIntent.putExtra("key1", this.currentLocation);
        startActivity(newPoiIntent);
    }

    private void addPOIOverlay(Location location) {
        PositionItemizedOverlay itemizedoverlay = new
                PositionItemizedOverlay(this.getResources().getDrawable(R.drawable.face_smile), this);
        itemizedoverlay.addOverlay(new OverlayItem("GIS II", "ETH Hönggerberg", new
                GeoPoint(location.getLatitude(), location.getLongitude())));
        myOpenMapView.getOverlays().add(itemizedoverlay);
    }

    @Override
    public void onLocationChanged(Location location) {

        // position speichern
        currentLocation = location;

        updatePoiOverlays();
    }

    @Override
    public void onResume() {
        super.onResume();

        updatePoiOverlays();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // We don't bother about status changes in this assignment.
    }

    @Override
    public void onProviderEnabled(String provider) {
        // We don't evaluate this in the assignment
    }

    @Override
    public void onProviderDisabled(String provider) {
        // We don't evaluate this in the assignment
    }

}
