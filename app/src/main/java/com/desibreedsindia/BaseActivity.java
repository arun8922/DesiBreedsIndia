package com.desibreedsindia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desibreedsindia.Utils.FontHelper;
import com.desibreedsindia.Utils.SessionSave;
import com.desibreedsindia.interfaces.APIResult;
import com.desibreedsindia.service.APIService_Volley_JSON;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static Dialog mDialog;
    public static String returnString;
    public static Dialog sDialog;
    public static String defaultCountryCode;
    public static String[] CountryCodeList;
    public static String Countrylist, delimiter;
    public static String[] countryCodShowing;
    public static String[] countrydata;
    public static String[] CountryOrdered;
    //    public static List<CountryListData> mCountryList = new ArrayList<>();
    static Calendar calendar;
    private static JSONObject jsonObj;
    private static JSONArray jsonArr;
    public final String TAG = getClass().getSimpleName();
    public Typeface tf;
    public int _hour;
    public String _ampm;
    public Dialog alertDialog;
    public Dialog alertmDialog;
    protected BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            int level = intent.getIntExtra("level", 0);
            Log.d(TAG, "onReceive: BATTERY PERCENTAGE-->" + level + "%");
        }
    };
    Dialog lDialog;
    Dialog commonProgressDialog;
    //public static MixpanelAPI mixpanelAPI;
    //public static JSONObject mixpanelObject;
    //public static MixpanelAPI.People mixpanelPeople;
    private RequestQueue queue;
    private boolean isInBackground;
    private PendingIntent pendingIntent;
    private int view;
    static View parentLayout;
    Dialog ProgressBarDialog;

    /**
     * clear all driver session variables used except getcoreconfig details
     */
    public static void clearsession(Context ctx) {
        try {
            SessionSave.saveSession("status", "", ctx);
            SessionSave.saveSession("Id", "", ctx);
            SessionSave.saveSession("trip_id", "", ctx);
            SessionSave.saveSession("Driver_locations", "", ctx);
            SessionSave.saveSession("driver_id", "", ctx);
            SessionSave.saveSession("Name", "", ctx);
            SessionSave.saveSession("company_id", "", ctx);
            SessionSave.saveSession("bookedby", "", ctx);
            SessionSave.saveSession("p_image", "", ctx);
            SessionSave.saveSession("Email", "", ctx);
            SessionSave.saveSession("corporate_name", "", ctx);
            SessionSave.saveSession("Corporate", "", ctx);
            SessionSave.saveSession("fuel_phone", "", ctx);
            SessionSave.saveSession("fuel_country_code", "", ctx);
            SessionSave.saveSession("fuel_password", "", ctx);
            SessionSave.saveSession("fuel_title", "", ctx);
            SessionSave.saveSession("fuel_web_android_login", "", ctx);
            SessionSave.saveSession("fuel_dashboard_url", "", ctx);
            SessionSave.saveSession("fuel_logout", "", ctx);
            SessionSave.saveSession("fuel_driver", "", ctx);
            SessionSave.saveSession("fuel_email", "", ctx);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // Showing the status in Snackbar
    public static void showSnack(final Context mContext, int isConnected, String message, final EditText editText) {
        if (isConnected==1) {
            final Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.WHITE)
                    .setAction(mContext.getResources().getString(R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            snackbar.show();
        } else if (isConnected==2) {

            Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.WHITE)
                    .setAction(mContext.getResources().getString(R.string.retry), new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Activity activity = (Activity) mContext;
                            activity.finish();
                            Intent intent = new Intent(mContext, mContext.getClass());
                            mContext.startActivity(intent);
                        }
                    });


            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
        else  {

            Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE)
                    .setAction(mContext.getResources().getString(R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editText.requestFocus();
                        }
                    });


            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }



    }

    public static void isConnect(final Context mContext, final boolean isconnect) {
        try {

            if (!isconnect) {
                final View view = View.inflate(mContext, R.layout.alert_view, null);
                showSnack(mContext, 2, mContext.getResources().getString(R.string.check_internet),null);

               /* sDialog = new Dialog(mContext, R.style.dialogwinddow);
                sDialog.setContentView(view);
                sDialog.setCancelable(false);
                sDialog.show();

                FontHelper.applyFontLang(mContext, sDialog.findViewById(R.id.alert_id));

                final TextView title_text = (TextView) sDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) sDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) sDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) sDialog.findViewById(R.id.button_failure);
                title_text.setText("" + mContext.getResources().getString(R.string.check_internet));
                message_text.setText("" + mContext.getResources().getString(R.string.check_internet));
                button_success.setText("" + mContext.getResources().getString(R.string.check_internet));
                button_failure.setText("" + mContext.getResources().getString(R.string.check_internet));
                button_success.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        if (!SessionSave.getSession("Id", mContext).equals("")) {
                            if (NetworkStatus.isOnline(mContext)) {
                                sDialog.dismiss();
                                Intent intent = new Intent(mContext, mContext.getClass());
                                Activity activity = (Activity) mContext;
                                activity.finish();
                                mContext.startActivity(intent);
                            } else {
                                Toast.makeText(mContext, "" + mContext.getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                button_failure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        sDialog.dismiss();
                        Activity activity = (Activity) mContext;
                        activity.finish();
                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });*/
            } else {
                try {

                    if (mContext != null) {

                        Activity activity = (Activity) mContext;
                        activity.finish();
                        Intent intent = new Intent(mContext, mContext.getClass());
                        mContext.startActivity(intent);

                    }
                   /* if (sDialog == null || sDialog.isShowing()) {

                        sDialog.dismiss();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void gpsalert(final Context mContext) {
        //if (!isconnect) {
            final View view = View.inflate(mContext, R.layout.close_alert_view, null);
            mDialog = new Dialog(mContext, R.style.NewDialog);
            mDialog.setContentView(view);

           *//* if (SessionSave.getSession("Lang", mContext).equals("my")) {
                FontHelper.applyFont(mContext, mDialog.findViewById(R.id.alert_id), "myanmar.ttf");
            } else {
                FontHelper.applyFont(mContext, mDialog.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
            }*//*

            FontHelper.applyFontLang(mContext, mDialog.findViewById(R.id.alert_id));

            mDialog.setCancelable(false);
            if (!mDialog.isShowing())
                mDialog.show();
            final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            *//*title_text.setText("" + mContext.getResources().getString(R.string.location_disable));
            message_text.setText("" + mContext.getResources().getString(R.string.location_enable));
            button_success.setText("" + mContext.getResources().getString(R.string.enable));*//*
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                    //mContext.startActivity(mIntent);
                    mContext.startActivity(mIntent);

                    mDialog.dismiss();

                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mDialog.dismiss();
                }
            });
        //}
            *//*else {
            try {
                if (mDialog != null)
                    mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*//*
    }*/

    /**
     * This is method to format date(dd-MM-yyyy hh:mm:ss a) eg:(21-MAY-2015 11:15:34 AM)
     */
    public static String date_conversion(String date_time) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            calendar = Calendar.getInstance(Locale.UK);
            java.util.Date yourDate = parser.parse(date_time);
            calendar.setTime(yourDate);
            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    /**
     * This is method to format month value (MMM) eg:(MAY)
     */
    private static String monthFullName(int monthnumber) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, monthnumber - 1);
        SimpleDateFormat sf = new SimpleDateFormat("MMM");
        sf.setCalendar(cal);
        String monthName = sf.format(cal.getTime());
        return monthName;
    }

    /**
     * This is method to format month value (dd) eg:(21)
     */
    private static String daytwodigit(int daynumber) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, daynumber - 1);
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        sf.setCalendar(cal);
        String day2digit = sf.format(cal.getTime());
        return day2digit;
    }

    /**
     * This is method to format month value (hh:mm:ss a) eg:(11:15:34 AM)
     */
    private static String timetwodigit(int year, int month, int day, int hr, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hr, min, sec);
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss a");
        sf.setCalendar(cal);
        String time2digit = sf.format(cal.getTime());
        return time2digit.toUpperCase();
    }

    public static String getDefaultCountryCode() {
        return defaultCountryCode;
    }

    public static void setDefaultCountryCode(String value) {
        defaultCountryCode = value;
    }

    public static String[] getCountryCodeList() {
        return CountryCodeList;
    }

    public static void setCountryCodeList(String[] CountryCodeListvalues) {

        CountryCodeList = CountryCodeListvalues;
    }

    public static String[] getCountryCode(Context mContext) {

        String[] mItems = null;

        jsonObj = new JSONObject();
        jsonArr = new JSONArray();
        String splitPipline, splitCode, splitCountry;
        for (int i = 0; i < SessionSave.getSession("country_code_list", mContext).split("\\|").length; i++) {
            splitPipline = SessionSave.getSession("country_code_list", mContext).split("\\|")[i];
            splitCode = splitPipline.split(",")[0];
            splitCountry = splitPipline.split(",")[1];
            mItems = callingJsonFunction(splitCode, splitCountry);
        }

        return mItems;
    }

    /*
    * Create Json of CountryDetails
    * */
    private static String[] callingJsonFunction(String splitCode, String splitCountry) {

        String[] mItems = null;

        try {
            JSONObject jsonInObj = new JSONObject();
            jsonInObj.put("telephone_code", splitCode);
            jsonInObj.put("country_name", changeStringCase(splitCountry));
            jsonArr.put(jsonInObj);
            jsonObj.put("country_detail", jsonArr);

            JSONObject jObj = new JSONObject();
            jObj.put("country_detail", sortJsonArray(jsonArr));
            // mItems = createModelClass(jObj);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return mItems;
    }

    /*
    * Create Model Class
    * */
    /*private static String[] createModelClass(JSONObject jsonInObj) {
        mCountryList.clear();
        String[] mItems = null;
        try {
            JSONArray jsonArray = jsonInObj.getJSONArray("country_detail");
            mItems = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                CountryListData mDateSet = new CountryListData(jsonArray.getJSONObject(i).getString("telephone_code"), jsonArray.getJSONObject(i).getString("country_name"));
                mCountryList.add(mDateSet);

                mItems[i] = jsonArray.getJSONObject(i).getString("telephone_code") + " " + jsonArray.getJSONObject(i).getString("country_name");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return mItems;
    }*/

    /*
    * Sort Country Name - I
    * */
    public static JSONArray sortJsonArray(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }
        Collections.sort(list, new SortBasedCountryName());
        JSONArray resultArray = new JSONArray(list);
        return resultArray;
    }

    /*
    * To make a Country name as Sentence Case of String ( myanmar --> Myanmar)
    * */
    public static String changeStringCase(String s) {

        final String DELIMITERS = " '-/";
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static void setMailInboxCount(Context context, TextView inboxView) {

        int mailCount = SessionSave.getIntSession("UNREAD_COUNT", context);

        if (mailCount > 0) {
            inboxView.setVisibility(View.VISIBLE);
            inboxView.setText(mailCount);
        } else
            inboxView.setVisibility(View.GONE);

    }

    /**
     * This is method for set the layout for the child activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            view = setLayout();
//            mMyStatus = new MystatusData(BaseActivity.this);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //setLocale();
//            isAppIsInBackground(BaseActivity.this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "seine.aerospace.newfoodkourt2",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
//                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                BaseActivity.setDefaultCountryCode("" + SessionSave.getSession("default_country_code", this));
                System.out.println("Countrycode name Mainact " + BaseActivity.getDefaultCountryCode());
                //Countrycodelistmethod();
                parentLayout = findViewById(android.R.id.content);

            } catch (Exception e) {
                BaseActivity.setDefaultCountryCode("" + 95);
            }



        /*Intent intent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/


            if (view != 0) {


                setContentView(view);
                setFont();
                Initialize();
                if (mDialog != null)
                    if (mDialog.isShowing())
                        mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

    }

    public abstract int setLayout();

    public abstract void Initialize();

    public abstract void setFont();


    public void circularProgress(Context mContext,boolean isLoading)
    {

        if(isLoading)
        {
            View view = View.inflate(mContext, R.layout.progress_bar, null);
            ProgressBarDialog = new Dialog(mContext, R.style.dialogwinddow);
            ProgressBarDialog.setContentView(view);
            ProgressBarDialog.setCancelable(false);
            if (!ProgressBarDialog.isShowing()) {
                ProgressBarDialog.show();
           }
        }else {
            if (ProgressBarDialog != null && (ProgressBarDialog != null || ProgressBarDialog.isShowing())) {
                ProgressBarDialog.cancel();
                ProgressBarDialog.hide();
                ProgressBarDialog.dismiss();
            }
        }

    }

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {
        Toast toast = Toast.makeText(contex, "" + message, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        /*if (SessionSave.getSession("Lang", BaseActivity.this).equals("en")) {
            Typeface face = Typeface.createFromAsset(contex.getAssets(), "JosefinSans-SemiBold.otf");
            v.setTypeface(face);
        } else {
            if (SessionSave.getSession("Font_Type", contex).equals("zawgyi")) {
                Typeface face = Typeface.createFromAsset(contex.getAssets(), "mynamar-zawgyi.ttf");
                v.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(contex.getAssets(), "myanmar-unicode.ttf");
                v.setTypeface(face);
            }

        }*/


        toast.show();
    }

    /**
     * This is method for show the Log
     */
    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * This is method for check the mail is valid by the use of regex class.
     */
    public boolean validdmail(String string) {
        // TODO Auto-generated method stub
        boolean isValid = false;
        String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * This is method for convert the InputStream value into StringBuilder
     */
    public StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    /**
     * This is method for check the Internet connection
     */
    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    /**
     * This is method for show progress bar
     */
    public void showLoading(Context context) {
        try {

            View view = View.inflate(context, R.layout.progress_bar, null);
            mDialog = new Dialog(context, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is method for convert the string value into MD5
     */
    public String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    /**
     * This is method for logout the user from their current session.
     */
    public void logout(final Context context) {
        try {
            final View view = View.inflate(context, R.layout.alert_view, null);
            final Dialog mDialog3 = new Dialog(context, R.style.dialogwinddow);
            mDialog3.setContentView(view);
            mDialog3.setCancelable(false);
            mDialog3.show();

           /* if (SessionSave.getSession("Lang", context).equals("my")) {
                FontHelper.applyFont(context, mDialog3.findViewById(R.id.alert_id), "myanmar.ttf");
            } else {
                FontHelper.applyFont(context, mDialog3.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
            }
*/
            FontHelper.applyFontLang(context, mDialog3.findViewById(R.id.alert_id));


            final TextView title_text = (TextView) mDialog3.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mDialog3.findViewById(R.id.message_text);
            final Button button_success = (Button) mDialog3.findViewById(R.id.button_success);
            final Button button_failure = (Button) mDialog3.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.VISIBLE);
            /*title_text.setText("" + context.getResources().getString(R.string.message));
            message_text.setText("" + context.getResources().getString(R.string.confirmlogout));
            button_success.setText("" + context.getResources().getString(R.string.yes));
            button_failure.setText("" + context.getResources().getString(R.string.no));*/
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mDialog3.dismiss();
                        // TODO Auto-generated method stub
                        JSONObject j = new JSONObject();
                        j.put("driver_id", SessionSave.getSession("Id", context));
                        j.put("shiftupdate_id", SessionSave.getSession("Shiftupdate_Id", context));
                        j.put("device_type", "1");

                        String url = "type=user_logout";
                        //new MixPanelData(getResources().getString(R.string.mp_api_btn_profile_user_logout), MixPanelEventType.TIME);
                        new Logout(url, j);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mDialog3.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is method for set the language configuration.
     */
    public void setLocale() {
        if (SessionSave.getSession("Lang", BaseActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "my", BaseActivity.this);// for setting myanmar as a default language
            SessionSave.saveSession("Font_Type", "zawgyi", BaseActivity.this);// for setting unicode or zawgyi font
            SessionSave.saveSession("Lang_Country", "my_MM", BaseActivity.this);
        }
        System.out.println("Lang" + SessionSave.getSession("Lang", BaseActivity.this));
        System.out.println("Lang_Country" + SessionSave.getSession("Lang_Country", BaseActivity.this));
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", BaseActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(arry[0], arry[1]);
        Locale.setDefault(new Locale(arry[0], arry[1]));
        BaseActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    /**
     * Custom alert dialog used in entire project.can call from anywhere with the following param Context,title,message,success and failure button text.
     */
    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {


        try {


            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertDialog = new Dialog(mContext, R.style.NewDialog);
            alertDialog.setContentView(view);
            alertDialog.setCancelable(true);

      /*  if (SessionSave.getSession("Lang", MainTestActivity.this).equals("my")) {
            FontHelper.applyFont(this, alertDialog.findViewById(R.id.alert_id), "myanmar.ttf");
        } else {
            FontHelper.applyFont(this, alertDialog.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
        }*/

            FontHelper.applyFontLang(this, alertDialog.findViewById(R.id.alert_id));
            if (!((Activity) mContext).isFinishing()) {
                alertDialog.show();
            }


            //alertDialog.show();
            final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAlertView(final Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {


            final View view = View.inflate(mContext, R.layout.close_alert_view, null);
            alertDialog = new Dialog(mContext, R.style.NewDialog);
            alertDialog.setContentView(view);
            alertDialog.setCancelable(false);

      /*  if (SessionSave.getSession("Lang", MainTestActivity.this).equals("my")) {
            FontHelper.applyFont(this, alertDialog.findViewById(R.id.alert_id), "myanmar.ttf");
        } else {
            FontHelper.applyFont(this, alertDialog.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
        }*/

            FontHelper.applyFontLang(this, alertDialog.findViewById(R.id.alert_id));
            if (!((Activity) mContext).isFinishing()) {
                alertDialog.show();
            }


            //alertDialog.show();
            final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertDialog.findViewById(R.id.button_failure);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(failure_txt);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertDialog.dismiss();
                    ((Activity) mContext).finish();

                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom alert dialog used in entire project for force update
     */
    public void force_update_alert(final Context mContext, String title, String message, String success, String fauiler) {


        final View view = View.inflate(mContext, R.layout.alert_view, null);
        final Dialog alert = new Dialog(mContext, R.style.NewDialog);
        alert.setContentView(view);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(true);

      /*  if (SessionSave.getSession("Lang", MainTestActivity.this).equals("my")) {
            FontHelper.applyFont(this, alert.findViewById(R.id.alert_id), "myanmar.ttf");
        } else {
            FontHelper.applyFont(this, alert.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
        }*/

        FontHelper.applyFontLang(this, alert.findViewById(R.id.alert_id));

        alert.show();
        final TextView title_text = (TextView) alert.findViewById(R.id.title_text);
        title_text.setTextColor(Color.BLACK);
        final TextView message_text = (TextView) alert.findViewById(R.id.message_text);
        message_text.setTextColor(Color.BLACK);
        final Button button_success = (Button) alert.findViewById(R.id.button_success);
        title_text.setText(title);
        message_text.setText(message);
        button_success.setText(success);
        button_success.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                alert.dismiss();
                try {
                    clearsession(BaseActivity.this);
                    SessionSave.saveSession("trip_id", "", BaseActivity.this);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException a) {
                    a.printStackTrace();
                    ShowToast(mContext, "Play store not available");
                }
            }
        });
    }


    /**
     * This is method to validate the field like Mail,Password,Name,Salutation etc and show the appropriate alert message.
     */
    public boolean validations(ValidateAction VA, EditText stringtovalidate, Context con) {
        String message = "";
        boolean result = false;
        switch (VA) {


            case isValidName:
                if (TextUtils.isEmpty(stringtovalidate.getText().toString().trim()))
                    message = "" + con.getResources().getString(R.string.warn_name);
                else
                    result = true;
                break;

            case isValidEmail:
                if (TextUtils.isEmpty(stringtovalidate.getText().toString().trim())) {
                    stringtovalidate.requestFocus();
                    message = "" + con.getResources().getString(R.string.warn_email);
                } else if (!validdmail(stringtovalidate.getText().toString().trim())) {
                    stringtovalidate.requestFocus();
                    message = "" + con.getResources().getString(R.string.warn_valid_email);
                } else {
                    result = true;
                }

                break;

            case isValidMobile:
                if (TextUtils.isEmpty(stringtovalidate.getText().toString().trim())) {
                    message = "" + con.getResources().getString(R.string.warn_mobile);
                    stringtovalidate.requestFocus();
                } else if (stringtovalidate.getText().toString().trim().startsWith("0")) {
                    message = "" + con.getResources().getString(R.string.warn_valid_mobile);
                    stringtovalidate.requestFocus();
                } else if (stringtovalidate.length() < 8) {
                    message = "" + con.getResources().getString(R.string.warn_valid_mobile);
                    stringtovalidate.requestFocus();
                } else {
                    result = true;
                }

                break;

            case isValidPassword:
                if (TextUtils.isEmpty(stringtovalidate.getText().toString().trim())) {

                    message = "" + con.getResources().getString(R.string.warn_password);
                    stringtovalidate.requestFocus();
                } else if (stringtovalidate.length() < 5) {
                    stringtovalidate.requestFocus();
                    message = "" + con.getResources().getString(R.string.warn_valid_password);
                } else {
                    result = true;
                }

                break;



        }
        if (!message.equals("")) {
            //alert_view(con, "" + getResources().getString(R.string.message), "" + message, "" + getResources().getString(R.string.ok), "");
            showSnack(con,3,message,stringtovalidate);
        }
        return result;
    }

    public void showProgressDialog(Context dialogContext) {

        try {

            View view = View.inflate(dialogContext, R.layout.progress_bar, null);
            commonProgressDialog = new Dialog(dialogContext, R.style.dialogwinddow);
            commonProgressDialog.setContentView(view);
            commonProgressDialog.setCancelable(false);
            if (!isFinishing()) {
                commonProgressDialog.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Close the progress Dialog
     */
    public void closeProgressDialog() {

        try {
            if (commonProgressDialog != null || commonProgressDialog.isShowing()) {
                commonProgressDialog.dismiss();
                commonProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Log.e("MainTestActivity", "onStart" + " ");


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
            Log.e("MainTestActivity", "ALARM_SERVICE Destroyed" + " ");
//            isAppIsInBackground(BaseActivity.this);

           /* if (isInBackground) {

                stopAlaramService();

            }*/

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Log.e("MainTestActivity", "onPause" + " ");


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.e("MainActivtiy ", "onResume");
//            isAppIsInBackground(BaseActivity.this);

            /*if (!isInBackground) {
                stopAlaramService();
            }*/

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Log.e("MainTestActivity", "ALARM_SERVICE Stop" + " ");
//            isAppIsInBackground(BaseActivity.this);

            /*if (isInBackground && !SessionSave.getSession("Email", BaseActivity.this).equals("")) {
                startAlaramService();
            }*/

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void startAlaramService() {
        try {
            Log.e("MainTestActivity", "ALARM_SERVICE start" + " ");

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int interval = 1000 * 60 * 20;//1000
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAlaramService() {
        try {
            Log.e("MainTestActivity", "ALARM_SERVICE Cancel" + " ");
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isAppIsInBackground(Context context) {
        isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        Log.e("MainTestActivity", "is Background  " + isInBackground);
        return isInBackground;
    }

    @SuppressLint("InlinedApi")
    public void LockConfiguration() {
        /*
         * if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } else { setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); }
		 */
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation = 0;

        // if the device's natural orientation is portrait:
        try {
            if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height) {
                switch (rotation) {
                    case Surface.ROTATION_0:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        setRequestedOrientation(orientation);
                        break;
                    case Surface.ROTATION_90:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        setRequestedOrientation(orientation);
                        break;
                    case Surface.ROTATION_180:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                        setRequestedOrientation(orientation);
                        break;
                    case Surface.ROTATION_270:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                        setRequestedOrientation(orientation);
                        break;
                    default:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        setRequestedOrientation(orientation);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void UnLockConfiguration() {
        boolean portrait_only = getResources().getBoolean(R.bool.portrait_only);
        if (!portrait_only) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }*/

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    //    Get location name from given latitude and longitude
    public void getAddressFromLatLng(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses = null;
        //String address = "";
        String myAddress = "";
        String city = "";
        String country = "";
        geocoder = new Geocoder(this, Locale.UK);
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);



            // city = addresses.get(0).getAddressLine(1);
            //country = addresses.get(0).getAddressLine(2);

          /*  if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                if (addresses.get(0).getAddressLine(1).length() > 0 || addresses.get(0).getAddressLine(1).length() != 0)
                    city = addresses.get(0).getAddressLine(1);
                if (addresses.get(0).getAddressLine(2).length() > 0 || addresses.get(0).getAddressLine(2).length() != 0)
                    country = addresses.get(0).getAddressLine(2);
            }*/


            if (addresses != null && addresses.size() > 0) {
                final Address address = addresses.get(0);

                // myAddress = getString(R.string.address_output_string, address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
               /* StringBuilder builder = new StringBuilder();
                int len = address.getMaxAddressLineIndex();
                if (len != -1) {
                    for (int i = 0; i <= len; i++) {
                        builder.append(address.getAddressLine(i));
                        builder.append(len == i ? "" : ", ");
                    }
                    myAddress = builder.toString();
                }*/
//                myAddress = myAddress.replace("null", "");
                //myAddress=address.getAdminArea();

                System.out.println("Test1"+address.getAdminArea());
                System.out.println("Test2"+address.getFeatureName());
                System.out.println("Test3"+address.getSubAdminArea());
                System.out.println("Test4"+address.getLocality());
                System.out.println("Test5"+address.getSubLocality());
                System.out.println("Test6"+address.getPremises());
                if(address.getSubLocality().equalsIgnoreCase("null")||address.getLocality().equalsIgnoreCase("null"))
                {
                    myAddress = "null";
                }else {
//                    myAddress=address.getSubLocality()+", "+address.getLocality();
                     StringBuilder builder = new StringBuilder();
                int len = address.getMaxAddressLineIndex();
                if (len != -1) {
                    for (int i = 0; i <= len; i++) {
                        builder.append(address.getAddressLine(i));
                        builder.append(len == i ? "" : ", ");
                    }
                    myAddress = builder.toString();
                }
                myAddress = myAddress.replace("null", "");
                }

            } else {
                myAddress = "null";
            }


            if (!myAddress.equalsIgnoreCase("null")) {

                Log.i(TAG, "getCurrentAddress: address : 1");

               /* if (!city.equalsIgnoreCase("null")) {

                    Log.i(TAG, "getCurrentAddress: address : 2");

                    if (!country.equalsIgnoreCase("null")) {

                        Log.i(TAG, "getCurrentAddress: address : 3");

                        SessionSave.saveSession("Driver_locations_home", "" + address + "\n" + city + "\n" + country, BaseActivity.this);

                    } else {

                        Log.i(TAG, "getCurrentAddress: address : 4");


                        SessionSave.saveSession("Driver_locations_home", "" + address + "\n" + city + "\n", BaseActivity.this);
                    }
                } else {

                    Log.i(TAG, "getCurrentAddress: address : 5");

                    SessionSave.saveSession("Driver_locations_home", "" + address, BaseActivity.this);

                }*/
                //SessionSave.saveSession("Current_Address", "" + myAddress, BaseActivity.this);
                requestingLocationConvert(lat, lon);
            } else {
                requestingLocationConvert(lat, lon);
            }


            Log.i("", "getAddressFromLatLng lat lng updating address:" + myAddress + ",city:" + city + ",country:" + country);

            //return SessionSave.getSession("Driver_locations_home", BaseActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
          /*  String loc = setCurrentLocationAddress(lat, lon);
            if (loc.equalsIgnoreCase(""))
                return "" + getResources().getString(R.string.locating_you);
            else {
                SessionSave.saveSession("Driver_locations_home", "" + loc, BaseActivity.this);
                return "" + loc;
            }*/
            requestingLocationConvert(lat, lon);
        }


    }

    // To set location as address from given lat lng.
    public String setCurrentLocationAddress(final double lat, final double lng) {
        String myAddress = "";
        try {
            final Geocoder geocoder = new Geocoder(this, Locale.UK);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);

            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
           /* if (addresses != null && addresses.size() > 0) {
                final Address address = addresses.get(0);
                myAddress = getString(R.string.address_output_string, address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
                myAddress = myAddress.replace("null", "");
            } else {
                myAddress = "";
            }*/

            if (addresses != null && addresses.size() > 0) {
                final Address address = addresses.get(0);

                // myAddress = getString(R.string.address_output_string, address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
                StringBuilder builder = new StringBuilder();
                int len = address.getMaxAddressLineIndex();
                if (len != -1) {
                    for (int i = 0; i <= len; i++) {
                        builder.append(address.getAddressLine(i));
                        builder.append(len == i ? "" : ", ");
                    }
                    myAddress = builder.toString();
                }
                myAddress = myAddress.replace("null", "");
            } else {
                myAddress = "";
            }


        } catch (Exception e) {
            e.printStackTrace();
            myAddress = "";
        }

        Log.i(TAG, "setCurrentLocationAddress: " + myAddress);

        return myAddress;
    }


    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    private void requestingLocationConvert(double lat, double lon) {
        try {
            String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=false&key=" + getResources().getString(R.string.GOOGLE_ASSET_KEY);
//ShowToast(this,"ghgh");


            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }

            @SuppressWarnings({"unchecked", "rawtypes"})
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, googleMapUrl, null, new Response.Listener() {
                @Override
                public void onResponse(Object result) {
                    setLocation(result.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(jsObjRequest);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void setLocation(String inputJson) {
        String City = "";
        try {
            JSONObject object = new JSONObject("" + inputJson);

            if(object.getString("status").equalsIgnoreCase("REQUEST_DENIED"))
            {
              ShowToast(this,object.getString("error_message"));
            }else {
                JSONArray array = object.getJSONArray("results");
                Log.d("Result", "     " + array);
                JSONObject array_obj = array.getJSONObject(0);

                SessionSave.saveSession("Current_Address", "" + array_obj.getString("formatted_address"), BaseActivity.this);
                Log.d("Address  ", " formatted_address     " + array_obj.getString("formatted_address"));
                JSONArray address_components = array_obj.getJSONArray("address_components");
                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject json_obj = address_components.getJSONObject(i);
                    String long_name = json_obj.getString("long_name");
                    JSONArray types = json_obj.getJSONArray("types");
                    String Type = types.getString(0);
                    Log.d("Address names ", " types     " + types);
                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            City = long_name;
                            SessionSave.saveSession("city_name", "" + City, BaseActivity.this);
                            Log.e("Address Google Api 111 ", " administrative_area_level_2 " + City);
                            break;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            City = long_name;
                            SessionSave.saveSession("city_name", "" + City, BaseActivity.this);
                            Log.e("Address Google Api 222", " administrative_area_level_1 " + City);
                            break;
                        } else {

                            SessionSave.saveSession("city_name", "", BaseActivity.this);
                            Log.e("Address Google Api 333", " Empty " + SessionSave.getSession("city_name", BaseActivity.this));
                        }
                    }


                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public enum ValidateAction {
        isValidName, isValidMobile, isValidEmail, isValidPassword, isValidCarotpCode1, isValidCarotpCode2, isValidCarotpCode3, isValidCarotpCode4, isValidCarotpCode5
    }


    public static class SortBasedCountryName implements Comparator<JSONObject> {
        /*
        * (non-Javadoc)
        *
        * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
        * lhs- 1st message in the form of json object. rhs- 2nd message in the form
        * of json object.
        */
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            try {
                return (lhs.getString("country_name").toLowerCase().compareTo(rhs.getString("country_name").toLowerCase()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * This is class for logout API call and process the response.Clear their current session.
     */
    private class Logout implements APIResult {
        public Logout(String url, JSONObject data) {
            System.out.println("" + url);
            System.out.println("" + data);
            if (isOnline()) {
                new APIService_Volley_JSON(BaseActivity.this, this, data, false).execute(url);
            } else {
                alert_view(BaseActivity.this, "" + getResources().getString(R.string.check_internet), "" + getResources().getString(R.string.check_internet), "" + getResources().getString(R.string.check_internet), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {

                        clearsession(BaseActivity.this);
                        final View view = View.inflate(BaseActivity.this, R.layout.alert_view, null);
                        lDialog = new Dialog(BaseActivity.this, R.style.NewDialog);
                        lDialog.setContentView(view);
                        lDialog.setCancelable(false);
                        lDialog.show();

                       /* if (SessionSave.getSession("Lang", MainTestActivity.this).equals("my")) {
                            FontHelper.applyFont(MainTestActivity.this, lDialog.findViewById(R.id.alert_id), "myanmar.ttf");
                        } else {
                            FontHelper.applyFont(MainTestActivity.this, lDialog.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
                        }*/
                        FontHelper.applyFontLang(BaseActivity.this, lDialog.findViewById(R.id.alert_id));

                        final TextView title_text = (TextView) lDialog.findViewById(R.id.title_text);
                        final TextView message_text = (TextView) lDialog.findViewById(R.id.message_text);
                        final Button button_success = (Button) lDialog.findViewById(R.id.button_success);
                        title_text.setText("" + getResources().getString(R.string.check_internet));
                        message_text.setText("" + json.getString("message"));
                        button_success.setText("" + getResources().getString(R.string.check_internet));
                        button_success.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                // TODO Auto-generated method stub
                                lDialog.dismiss();
                                clearsession(BaseActivity.this);
                                //Intent intent = new Intent(MainTestActivity.this, UserLoginAct.class);
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                ComponentName cn = new ComponentName(BaseActivity.this, UserLoginAct.class);
//                                intent.setComponent(cn);
                                startActivity(intent);
                                finish();
                            }
                        });
                        // new MixPanelData(getResources().getString(R.string.mp_api_btn_profile_user_logout), MixPanelEventType.TRACK);
                    } else {
                        alert_view(BaseActivity.this, "" + getResources().getString(R.string.check_internet), "" + json.getString("message"), "" + getResources().getString(R.string.check_internet), "");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                alert_view(BaseActivity.this, "" + getResources().getString(R.string.check_internet), "" + getResources().getString(R.string.check_internet), "" + getResources().getString(R.string.check_internet), "");
            }
        }
    }


/*
    class buttonColorFilledTextWatcher implements TextWatcher {
        EditText getName, getMobile, getEmail, getPassword;
        Button getSignIn, getSignUp;
        int status = 0;

        buttonColorFilledTextWatcher(EditText name, EditText mobile, EditText email, EditText password, Button signUp) {
            getName = name;
            getMobile = mobile;
            getEmail = email;
            getPassword = password;
            getSignUp = signUp;
            status = 1;
        }

        public buttonColorFilledTextWatcher(EditText mobile, EditText password, Button signIn) {

            getMobile = mobile;
            getPassword = password;
            getSignIn = signIn;
            status = 2;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (status == 1) {
                if ((!getName.getText().toString().isEmpty()) && (!getMobile.getText().toString().isEmpty()) && (!getEmail.getText().toString().isEmpty()) && (!getPassword.getText().toString().isEmpty())) {
                    getSignUp.setBackgroundResource(R.drawable.rounded_corner_filled);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getSignUp.setTextColor(getColor(R.color.white));
                    } else {
                        getSignUp.setTextColor(getResources().getColor(R.color.white));
                    }
                } else {
                    getSignUp.setBackgroundResource(R.drawable.rounded_corner);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getSignUp.setTextColor(getColor(R.color.theme_green));
                    } else {
                        getSignUp.setTextColor(getResources().getColor(R.color.theme_green));
                    }
                }
            } else {
                if ((!getMobile.getText().toString().isEmpty()) && (!getPassword.getText().toString().isEmpty())) {
                    getSignIn.setBackgroundResource(R.drawable.rounded_corner_filled);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getSignIn.setTextColor(getColor(R.color.white));
                    } else {
                        getSignIn.setTextColor(getResources().getColor(R.color.white));
                    }
                } else {
                    getSignIn.setBackgroundResource(R.drawable.rounded_corner);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getSignIn.setTextColor(getColor(R.color.theme_green));
                    } else {
                        getSignIn.setTextColor(getResources().getColor(R.color.theme_green));
                    }
                }
            }

        }
    }
*/


}
