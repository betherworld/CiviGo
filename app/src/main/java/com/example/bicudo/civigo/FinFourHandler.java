package com.example.bicudo.civigo;

import android.app.Application;
import android.content.Context;

//import com.android.volley.Request;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class FinFourHandler {
    private String baseUrl = "http://www.finfour.net";
    RequestQueue queue;
    public FinFourHandler(){
        this.queue = Volley.newRequestQueue(GlobalApplication.getAppContext());
    }

    public void Login() throws JSONException {

        JSONObject params = new JSONObject();
        params.put("email", "proposer@gmail.com");
        params.put("password", "proposer");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + "/wapi/login",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.toString());
                    }
                }
        );
        this.queue.add(request);
    }
}
