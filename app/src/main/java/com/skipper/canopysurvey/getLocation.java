package com.skipper.canopysurvey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class getLocation extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {
    //Apparently this is used in settings dialogue...
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    //Update Frequency
    public static final long UPDATE_INTERVAL = 5000;

    public static final long FASTEST = UPDATE_INTERVAL / 2;

      protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    protected TextView latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        latitude = (TextView) findViewById(R.id.lat);
        longitude = (TextView) findViewById(R.id.lng);


        buildGoogleApiClient();
    }

    protected void deleteButton (View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            Log.d("Client", "Null");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        Log.d("Connected", "Client is connected");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasGPSPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasGPSPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CHECK_SETTINGS);
                Log.d("Permission","Asked for permission");
                return;
            }else{startLocationUpdate();}
        }




    }

    private void startLocationUpdate() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasGPSPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasGPSPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},123);
                Log.d("Permission","Asked for permission");
                return;
            }
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation !=null){
            String lat = Double.toString(mCurrentLocation.getLatitude());
            String lng = Double.toString(mCurrentLocation.getLongitude());
            latitude.setText(lat);
            longitude.setText(lng);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.d("Connection status: ",connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        //What do we do with a drunken sailor? Find out the lat and Lng
        String lat = Double.toString(location.getLatitude());
        String lng = Double.toString(location.getLongitude());
        latitude.setText(lat);
        longitude.setText(lng);
        Log.d("Updated","Location Updated");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case REQUEST_CHECK_SETTINGS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdate();
                    Log.d("Has permissions","User granted permissions");
                } else {
                    Toast.makeText(this, "ERROR: You must enable GPS for this to work", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }
}
