package com.waitskipper.logic;

import android.location.Location;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waitskipper.model.Establishment;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by acheron0 on 2/7/2015.
 */
public class EstablishmentManager implements ILocationListener
{
    static final String BASE_URI = "http://protowaitup.elasticbeanstalk.com/";

    private RequestQueue queue;
    private IEstablishmentListener establishmentListener;

    public EstablishmentManager(RequestQueue queue, IEstablishmentListener establishmentListener)
    {
        this.queue = queue;
        this.establishmentListener = establishmentListener;
    }

    public void updateLocation(Location location)
    {
        String url =  BASE_URI + "locations/" + location.getLatitude() + "/" + location.getLongitude() + "/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o)
                    {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Establishment>>() {}.getType();
                        ArrayList<Establishment> establishments = gson.fromJson((String)o, listType);

                        establishmentListener.establishmentsChanged(establishments);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               throw new RuntimeException(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
