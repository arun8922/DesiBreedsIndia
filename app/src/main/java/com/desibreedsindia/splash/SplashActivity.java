package com.desibreedsindia.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.desibreedsindia.BaseActivity;
import com.desibreedsindia.R;
import com.desibreedsindia.Utils.Config;
import com.desibreedsindia.Utils.NotificationUtils;
import com.desibreedsindia.interfaces.APIResult;
import com.desibreedsindia.service.APIService_Volley_JSON_No_Progress;
import com.desibreedsindia.signIn.PhoneVerificationActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import static com.desibreedsindia.BaseActivity.showSnack;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_splash);
//
//
//
//    }

    @Override
    public int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void Initialize() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//llk
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();
    }

    @Override
    public void setFont() {

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        new BaseAPI();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private class BaseAPI implements APIResult {
        String msg = "";


        BaseAPI() {

            try {
                if (isOnline()) {
                    new APIService_Volley_JSON_No_Progress(SplashActivity.this, this, true).execute("splash_api");
                } else {
                    //alert_view(SplashActivity.this,getResources().getString(R.string.message),getResources().getString(R.string.check_internet),getResources().getString(R.string.ok), "");
                    showSnack(SplashActivity.this, 2, getResources().getString(R.string.check_internet), null);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    msg = json.getString("message");
//                    alert_view(SplashActivity.this,getResources().getString(R.string.message), msg,getResources().getString(R.string.ok), "");

                    Intent intent = new Intent(SplashActivity.this, PhoneVerificationActivity.class);
                    startActivity(intent);
                    finish();
                   /* if (json.getInt("status") == 1) {
                        try {
                            Intent intent = new Intent(SplashActivity.this, PhoneVerificationActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }  else {

                        showSnack(SplashActivity.this, 1, getResources().getString(R.string.message), null);

                    }*/
                } else {
//                    alert_view(SplashActivity.this,getResources().getString(R.string.message),getResources().getString(R.string.internal_error),getResources().getString(R.string.ok), "");
                    showSnack(SplashActivity.this, 2, getResources().getString(R.string.check_internet), null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
