package com.example.bicudo.civigo;

import android.app.Application;
import android.arch.core.util.Function;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;

//import com.android.volley.Request;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.android.volley.toolbox.*;

public class FinFourHandler {
    private String baseUrl = "http://www.finfour.net";
    RequestQueue queue;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public FinFourHandler(){
        this.queue = Volley.newRequestQueue(GlobalApplication.getAppContext());

        DefaultHttpClient httpclient = new DefaultHttpClient();

        CookieStore cookieStore = new BasicCookieStore();
        httpclient.setCookieStore( cookieStore );

        HttpStack httpStack = new HttpClientStack( httpclient );
        this.queue = Volley.newRequestQueue( GlobalApplication.getAppContext(), httpStack  );
    }

    public void Login() throws JSONException, InterruptedException {
        //final CountDownLatch latch = new CountDownLatch (1);

        JSONObject params = new JSONObject();
        params.put("email", "proposer@gmail.com");
        params.put("password", "proposer");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + "/wapi/login",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        //latch.countDown();
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
        //latch.await();
    }

    //new post
    public static class EntryType{
        static final int Proposal = 255;
        static final int Mending = 256;
    };
    public void newEntry(int entryType, String description, Image image) throws JSONException, InterruptedException{
        //final CountDownLatch latch = new CountDownLatch (1);

        JSONObject params = new JSONObject();
        params.put("assetId", entryType);
        params.put("blockText", description);
        params.put("images", "[]");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + "/wapi/v2/asset-blocks",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        //latch.countDown();
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
        //latch.await();
    }


    //timelines
    public void getEntries(int entryType, final Function<JSONObject, Void> callback) throws JSONException, InterruptedException{
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, baseUrl + "/wapi/v2/timeline/asset/" + Integer.toString(entryType),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.apply(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.toString());
                    }
                }
        );
    }

    //current users tokens
    public void getTokens(final Function<JSONObject, JSONObject> callback){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, baseUrl + "/wapi/balances?",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.apply(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.toString());
                    }
                }
        );
    }
}
