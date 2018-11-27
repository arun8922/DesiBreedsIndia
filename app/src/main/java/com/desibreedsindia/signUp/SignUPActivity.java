package com.desibreedsindia.signUp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.desibreedsindia.BaseActivity;
import com.desibreedsindia.HomeActivity;
import com.desibreedsindia.R;
import com.desibreedsindia.Utils.SessionSave;
import com.desibreedsindia.interfaces.APIResult;
import com.desibreedsindia.service.APIService_Volley_JSON;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUPActivity extends BaseActivity {

    private AdView mAdView;
    private ImageView profileImage;
    private TextInputEditText name,mobile,email,password,address;
    private Button btnSignUp;
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

    }*/

    @Override
    public int setLayout() {
        return R.layout.sign_up;
    }

    @Override
    public void Initialize() {
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);

        name=(TextInputEditText)findViewById(R.id.input_name);
        mobile=(TextInputEditText)findViewById(R.id.input_mobile);
        mobile.setEnabled(false);
        email=(TextInputEditText)findViewById(R.id.input_email);
        password=(TextInputEditText)findViewById(R.id.input_password);
        address=(TextInputEditText)findViewById(R.id.input_address);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mobile.setText(extras.getString("phone"));
        }

        mAdView = (AdView) findViewById(R.id.adView);


        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFF5454B0774708339BC273F8A43708")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new RegisterAPI();
                Intent intent=new Intent(SignUPActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setFont() {

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private class RegisterAPI implements APIResult {
        String msg = "";

        RegisterAPI() {
            try {

                JSONObject inputData = new JSONObject();
                inputData.put("device_token", SessionSave.getSession("fcmToken", SignUPActivity.this));
                inputData.put("fcm_token", SessionSave.getSession("fcmToken", SignUPActivity.this));
                inputData.put("device_type", "1");
                inputData.put("name", name.getText().toString().trim());
                inputData.put("mobile_number", mobile.getText().toString().trim());
                inputData.put("email", email.getText().toString().trim());
                inputData.put("password", convertPassMd5(password.getText().toString().trim()));
                inputData.put("os_version", "");
                inputData.put("profile_image", "");
                inputData.put("latitude", "");
                inputData.put("longitude","");
                inputData.put("model_name","");

                if (isOnline()) {
                    new APIService_Volley_JSON(SignUPActivity.this, this, inputData, false).execute("user_signup");
                } else {
                    alert_view(SignUPActivity.this, getResources().getString(R.string.message), getResources().getString(R.string.check_internet), getResources().getString(R.string.ok), "");
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

                    if (json.getInt("status") == 1) {
                        try {


                            Intent intent=new Intent(SignUPActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    } else {

                        alert_view(SignUPActivity.this, getResources().getString(R.string.message), msg, getResources().getString(R.string.ok), "");

                    }
                } else {
                    alert_view(SignUPActivity.this, getResources().getString(R.string.message), getResources().getString(R.string.check_internet), getResources().getString(R.string.ok), "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
