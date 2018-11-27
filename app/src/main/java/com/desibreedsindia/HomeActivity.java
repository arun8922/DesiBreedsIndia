package com.desibreedsindia;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desibreedsindia.Utils.FontHelper;
import com.desibreedsindia.adapter.AdsListAdapter;
import com.desibreedsindia.adapter.CatListAdapter;
import com.desibreedsindia.data.AdListData;
import com.desibreedsindia.data.CatListData;
import com.desibreedsindia.interfaces.APIResult;
import com.desibreedsindia.service.APIService_Volley_JSON;
import com.desibreedsindia.signUp.SignUPActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private android.support.v7.app.AlertDialog mAlertDialog;
    private boolean alertShown = true;

    private boolean isGPSEnabled;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager = null;
    private SwipyRefreshLayout mSwipyAdRefreshLayout,mSwipyCatRefreshLayout;
    private RecyclerView adList, categoryList;
    private RecyclerView.Adapter mAdAdapter,mCatAdapter;
    private RecyclerView.LayoutManager mAdLayoutManager, mCategoryLayoutManager;
    private TextView noData, mToolbarTile;
    private int pageIndex = 1;
    private ArrayList<CatListData> CatArrayListData = new ArrayList<>();
    private ArrayList<AdListData> AdArrayListData = new ArrayList<>();
    private CatListData catListData;
    private AdListData adListData;
    private LinearLayout mSellButton;


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int LOCATION_REQUEST_SETTING = 11;

    @Override
    public int setLayout() {

        return R.layout.activity_home;
    }

    @Override
    public void Initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(null);
        /*toolbar.setTitleTextColor(getResources().getColor(R.color.theme_green));*/
        mSwipyCatRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe_catlayout);
        mSwipyAdRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe_adlayout);
        mToolbarTile = (TextView) findViewById(R.id.toolbar_title);
        mSellButton = (LinearLayout) findViewById(R.id.sell_ad);

        categoryList = (RecyclerView) findViewById(R.id.cat_recyclerView);
        categoryList.setHasFixedSize(true);
        mCategoryLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        categoryList.setLayoutManager(mCategoryLayoutManager);

        adList = (RecyclerView) findViewById(R.id.ad_recyclerView);
        adList.setHasFixedSize(true);
        mAdLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adList.setLayoutManager(mAdLayoutManager);



        noData = (TextView) findViewById(R.id.nodata);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setBackgroundColor(getResources().getColor(R.color.colorAccent));// screen bg color
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mSwipyAdRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainTestActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                Log.d("MailActivity Count", "Refresh triggered at ");

/* mSwipyRefreshLayout.setRefreshing(false);
                    mSwipyRefreshLayout.setEnabled(false);*/


                if (AdArrayListData.size() > 8) {
                    new HotelList(pageIndex);
                } else {
                    mSwipyAdRefreshLayout.setEnabled(false);
                    mSwipyAdRefreshLayout.setRefreshing(false);
                }


            }
        });

        mSwipyAdRefreshLayout.setColorSchemeResources(R.color.theme_green,
                R.color.theme_red,
                R.color.theme_green,
                R.color.theme_red);

        buildGoogleApiClient();

        new HotelList(pageIndex);


        mSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, SignUPActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setFont() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (alertShown) {
//                allowPermissions();
            }
        } else {
//            APICallWithGPS();
        }
        APICallWithGPS();
    }


    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //.addApi(Places.GEO_DATA_API)
                //.addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    // Onstart method by default it called when activity is open.
    @Override
    public void onStart() {
        super.onStart();
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA);
    }


    private void allowPermissions() {
        alertShown = false;
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(getResources().getString(R.string.need_permission));
                builder.setCancelable(false);
                builder.setMessage(getResources().getString(R.string.permission_description));
                builder.setPositiveButton(getResources().getString(R.string.grant_permission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                       /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);*/
                    }
                });
                mAlertDialog = builder.create();
                if (mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog.show();
                } else {
                    mAlertDialog.show();
                }


                ShowToast(HomeActivity.this, "2");
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(HomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                ShowToast(HomeActivity.this, "4");
            }
            ShowToast(HomeActivity.this, "1");
        } else {
            //You already have the permission, just go ahead.
//            proceedAfterPermission();
            APICallWithGPS();
//            Toast.makeText(getBaseContext(), "Success--2", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT)
                    .show();
        }*/
Toast.makeText(this,"hhvghh",Toast.LENGTH_SHORT).show();

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[2]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
//                proceedAfterPermission();
                APICallWithGPS();
//                Toast.makeText(getBaseContext(), "Success--4", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
        } else if (requestCode == LOCATION_REQUEST_SETTING) {
            APICallWithGPS();
        }

    }


    private void APICallWithGPS() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if (mLocationManager != null) {
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        }
        if (isGPSEnabled) {

                   /* if (VersionCheck() && NetworkStatus.isOnline(SplashAct.this))
                        versionAlert(SplashAct.this);
                    else */
            if (isOnline()) {
                try {
                    Toast.makeText(this, "njnjn", Toast.LENGTH_LONG).show();

                    new HotelList(pageIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                        ShowToast(SplashAct.this, "" + getResources().getString(R.string.check_net_connection));
                isConnect(HomeActivity.this, false);
            }
        } else {
            gpsalert(HomeActivity.this);
        }
    }

    public void gpsalert(final Context mContext) {
        //if (!isconnect) {
        final View view = View.inflate(mContext, R.layout.close_alert_view, null);
        mDialog = new Dialog(mContext, R.style.NewDialog);
        mDialog.setContentView(view);

           /* if (SessionSave.getSession("Lang", mContext).equals("my")) {
                FontHelper.applyFont(mContext, mDialog.findViewById(R.id.alert_id), "myanmar.ttf");
            } else {
                FontHelper.applyFont(mContext, mDialog.findViewById(R.id.alert_id), "JosefinSans-SemiBold.otf");
            }*/

        FontHelper.applyFontLang(mContext, mDialog.findViewById(R.id.alert_id));

        mDialog.setCancelable(false);
        if (!mDialog.isShowing())
            mDialog.show();
        final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.GONE);
            /*title_text.setText("" + mContext.getResources().getString(R.string.location_disable));
            message_text.setText("" + mContext.getResources().getString(R.string.location_enable));
            button_success.setText("" + mContext.getResources().getString(R.string.enable));*/
        button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                //mContext.startActivity(mIntent);
                startActivityForResult(mIntent, LOCATION_REQUEST_SETTING);

                mDialog.dismiss();

            }
        });
        button_failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mDialog.dismiss();
            }
        });
        //}
            /*else {
            try {
                if (mDialog != null)
                    mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {

            startLocationUpdates();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            getAddressFromLatLng(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude(), LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        try {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

           /* mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(6000); // two minute interval
            mLocationRequest.setFastestInterval(6000);//120000
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/
            /*if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }*/
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, null);
//            requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        try {
           /* if (mFusedLocationClient != null) {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }*/
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class HotelList implements APIResult {
        String msg = "";

        HotelList(int page_index) {
            JSONObject data = new JSONObject();
            try {
                //11.0195207  //76.9616311

                data.put("user_id", "1");
                data.put("current_latitude", "");
                data.put("current_longitude", "");
                data.put("km_range", "");
                data.put("category_id", "");
                data.put("sub_category_id", "");
                data.put("min_amount", "");
                data.put("max_amount", "");
                data.put("min_age", "");
                data.put("max_age", "");
//                data.put("page_id", String.valueOf(page_index));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isOnline()) {

                new APIService_Volley_JSON(HomeActivity.this, this, data, false).execute("get_ads");
            } else {
                alert_view(HomeActivity.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet), "" + getResources().getString(R.string.ok), "");
            }

        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            try {
                if (isSuccess) {

                    JSONObject json = new JSONObject(result);

                    if (json.getInt("status") == 1) {
                        try {

                          /*  if (pageIndex == 1) {
                                HotelArrayListData.clear();
                            }*/
                            JSONArray jArry = json.getJSONArray("details");

                            int length = jArry.length();
                            for (int i = 0; i < length; i++) {
                                adListData = new AdListData(jArry.getJSONObject(i).getString("post_id"), jArry.getJSONObject(i).getString("category_id"), jArry.getJSONObject(i).getString("sub_category_id"), jArry.getJSONObject(i).getString("seller_id"), jArry.getJSONObject(i).getString("ad_title"), jArry.getJSONObject(i).getString("ad_description"), jArry.getJSONObject(i).getString("ad_price"), jArry.getJSONObject(i).getString("ad_image1"));
                                AdArrayListData.add(adListData);
                                catListData = new CatListData(jArry.getJSONObject(i).getString("category_id"),jArry.getJSONObject(i).getString("ad_image1"));

                                CatArrayListData.add(catListData);
                            }
//                            ShowToast(HomeActivity.this,""+jArry.length());
                            noData.setVisibility(View.GONE);


                            mCatAdapter = new CatListAdapter(HomeActivity.this, CatArrayListData);
                            categoryList.setItemAnimator(new DefaultItemAnimator());
                            categoryList.setAdapter(mCatAdapter);



                            mAdAdapter = new AdsListAdapter(HomeActivity.this, AdArrayListData);
                            adList.setItemAnimator(new DefaultItemAnimator());
                            adList.setAdapter(mAdAdapter);
                            adList.getLayoutManager().scrollToPosition(AdArrayListData.size() - jArry.length());
                            adList.setVisibility(View.VISIBLE);

//                            mAdapter.notifyDataSetChanged();
                            if (AdArrayListData.size() > 8) {
                                pageIndex++;

                                mSwipyAdRefreshLayout.setRefreshing(false);

                            }
                            mSwipyAdRefreshLayout.setEnabled(true);

                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }

                    } else if (json.getInt("status") == 2) {
                        msg = json.getString("message");
                        mSwipyAdRefreshLayout.setRefreshing(false);
                        mSwipyAdRefreshLayout.setEnabled(false);
                        pageIndex = 1;
                    } else {

                        msg = json.getString("message");
//                        adList.setVisibility(View.GONE);
//                        noData.setVisibility(View.VISIBLE);
                        noData.setText(msg);
                        mSwipyAdRefreshLayout.setRefreshing(false);
                        pageIndex = 1;
                    }
                } /*else {
                    alert_view(HomeActivity.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.internal_error), "" + getResources().getString(R.string.ok), "");
                    mSwipyRefreshLayout.setRefreshing(false);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
