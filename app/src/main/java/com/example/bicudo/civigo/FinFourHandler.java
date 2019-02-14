package com.example.bicudo.civigo;

import android.app.Application;
import android.content.Context;

//import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class FinFourHandler {
    private String baseUrl = "https://www.finfour.net";
    RequestQueue queue;
    public FinFourHandler(){
        this.queue = Volley.newRequestQueue(GlobalApplication.getAppContext());
    }

    public void Login() throws JSONException {

        StringRequest request = new StringRequest(StringRequest.Method.POST, baseUrl + "/wapi/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "proposer@gmail.com");
                params.put("password", "proposer");
                System.out.println(params.toString());

                return params;
            }
        };
        this.queue.add(request);
    }
}
