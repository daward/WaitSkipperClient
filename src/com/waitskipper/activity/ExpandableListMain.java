package com.waitskipper.activity;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.waitskipper.model.Establishment;
import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class ExpandableListMain extends
        ExpandableListActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    //Initialize variables
//    private static final String STR_CHECKED = " has Checked!";
//    private static final String STR_UNCHECKED = " has unChecked!";
//    private int EstablishmentClickStatus = -1;
//    private int ChildClickStatus = -1;
    private ArrayList<Establishment> establishments;

    private RequestQueue queue;

    static final int AUTH_REQUEST = 1;
    static final String BASE_URI = "http://protowaitup.elasticbeanstalk.com/";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        getExpandableListView().setDividerHeight(3);
        registerForContextMenu(getExpandableListView());

        startActivityForResult(new Intent(this, AuthActivity.class), AUTH_REQUEST);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == AUTH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "GPS FOUND", Toast.LENGTH_LONG);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void disconnect()
    {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not connect " + connectionResult.toString(), Toast.LENGTH_LONG);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        String url =  BASE_URI + "locations/" + location.getLatitude() + "/" + location.getLongitude() + "/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        LoadData(location);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void LoadData(Location location)
    {
        //Creating static data in arraylist
        final ArrayList<Establishment> establishments = createModel(location);

        // Adding ArrayList data to ExpandableListView values
        loadHosts(establishments);
    }

    /**
     * here should come your data service implementation
     *
     * @return
     */
    private ArrayList<Establishment> createModel(Location location)
    {

        // Creating ArrayList of type Establishment class to store Establishment class objects
        final ArrayList<Establishment> list = new ArrayList<Establishment>();
        for (int i = 1; i < 4; i++) {
            //Create Establishment class object
            final Establishment Establishment = new Establishment();

            Establishment.setName("Restaurant - " + location.getLatitude() + ", " + location.getLongitude());

            //Adding Establishment class object to ArrayList
            list.add(Establishment);
        }
        return list;
    }

    private void loadHosts(final ArrayList<Establishment> newEstablishments)
    {
        if (newEstablishments == null)
            return;

        establishments = newEstablishments;

        // Check for ExpandableListAdapter object
        if (this.getExpandableListAdapter() == null)
        {
            //Create ExpandableListAdapter Object
            final ExpandableListAdapter mAdapter = new ExpandableListAdapter(getBaseContext(), establishments);

            // Set Adapter to ExpandableList Adapter
            this.setListAdapter(mAdapter);
        }
        else
        {
            // Refresh ExpandableListView data
            ((ExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
        }
    }
}
