package com.waitskipper.logic;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by acheron0 on 2/7/2015.
 */
public class LocationManager implements
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float JITTER = 5;

    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;
    private ILocationListener locationListener;
    private Location currentLocation;

    public LocationManager(GoogleApiClient.Builder apiBuilder, ILocationListener locationListener)
    {
        this.locationListener = locationListener;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);

        mGoogleApiClient = apiBuilder.addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i("Logger", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Logger", "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if(this.currentLocation == null || location.distanceTo(this.currentLocation) > JITTER)
        {
            currentLocation = location;
            locationListener.updateLocation(location);
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void connect()
    {
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    public void pause()
    {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
