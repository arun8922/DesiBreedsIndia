package com.desibreedsindia.volley;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.desibreedsindia.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

//		ACRA.init(this);
        mInstance = this;
        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this, getString(R.string.admob_app_id));


       /* Runtime runtime = Runtime.getRuntime();
        long maxMemory=runtime.maxMemory();
        long usedMemory=runtime.totalMemory() - runtime.freeMemory();
        long availableMemory=maxMemory-usedMemory;
        String formattedmaxMemory= Formatter.formatShortFileSize(this,maxMemory);
        String formattedusedMemory= Formatter.formatShortFileSize(this,usedMemory);
        String formattedavailableMemory= Formatter.formatShortFileSize(this,availableMemory);

        Log.e(TAG, "Runtime>>>"+ " maxMemory  "+maxMemory);
        Log.e(TAG, "Runtime>>>"+ " usedMemory  "+usedMemory);
        Log.e(TAG, "Runtime>>>"+ " availableMemory  "+availableMemory);

        Log.e(TAG, "Runtime>>>"+ " formattedmaxMemory  "+formattedmaxMemory);
        Log.e(TAG, "Runtime>>>"+ " formattedusedMemory  "+formattedusedMemory);
        Log.e(TAG, "Runtime>>>"+ " formattedavailableMemory  "+formattedavailableMemory);*/



    }




    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    //    To cancel pending api request
    public void cancelPendingApiReqQueue(String apiTag) {

        Log.i(TAG, "cancelPendingRequests: ");

        try {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(apiTag);
                Log.i(TAG, "cancelPendingRequests: cancelleddddddddddd");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory: onLowMemoryonLowMemoryonLowMemoryonLowMemoryonLowMemory");
    }*/
}
