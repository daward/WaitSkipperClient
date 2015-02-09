package com.waitskipper.activity;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.waitskipper.logic.EstablishmentManager;
import com.waitskipper.logic.LocationManager;

public class ExpandableListMain extends ExpandableListActivity
{
    private LocationManager locationManager;

    private EstablishmentManager establishmentManager;

    private ExpandableListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!servicesAvailable()) {
            finish();
        }

        mAdapter = new ExpandableListAdapter(getBaseContext());
        establishmentManager = new EstablishmentManager(Volley.newRequestQueue(this), mAdapter);
        locationManager = new LocationManager(new GoogleApiClient.Builder(this), establishmentManager);

        getExpandableListView().setDividerHeight(3);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

       locationManager.pause();
    }

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }
}
