package com.desibreedsindia.service;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.desibreedsindia.R;
import com.desibreedsindia.Utils.CommonData;
import com.desibreedsindia.Utils.NetworkStatus;
import com.desibreedsindia.interfaces.APIResult;
import com.desibreedsindia.volley.AppController;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * @author developer This AsyncTask used to communicate the application with server through Volley framework. Here the response completely in JSON format. Constructor get the input details List<NameValuePair>,POST or GET then url. In pre execute, Show the progress dialog. In Background,Connect and get the response. In Post execute, Return the result with interface. This class call the API without any progress on UI.
 */
public class APIService_Volley_JSON extends AsyncTask<String, String, String> {
    final String check_internet = "Check your Internet Connection";
    final String please_try_again = "Please try again later!";
    public Dialog mProgressdialog;
    public Context mContext;
    HashMap<String, String> map;
    StringRequest ObjReq;
    int requestingMethod = 0;
    String result = "";
    JsonObjectRequest JOR;
    private boolean isSuccess = true;
    private boolean GetMethod = true;
    private Dialog volleyProgressBarDialog;
    private JSONObject data;
    private APIResult response;
    private InputStream in;

    public APIService_Volley_JSON(Context ctx, APIResult res, JSONObject j, boolean getmethod) {
        mContext = ctx;
        response = res;
        this.data = j;
        GetMethod = getmethod;
    }

    public APIService_Volley_JSON(Context ctx, APIResult res, boolean getmethod) {
        mContext = ctx;
        response = res;
        GetMethod = getmethod;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (NetworkStatus.isOnline(mContext)) {

                View view = View.inflate(mContext, R.layout.progress_bar, null);
                volleyProgressBarDialog = new Dialog(mContext, R.style.dialogwinddow);
                volleyProgressBarDialog.setContentView(view);
                volleyProgressBarDialog.setCancelable(false);
                if (!volleyProgressBarDialog.isShowing()) {
                    volleyProgressBarDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (!NetworkStatus.isOnline(mContext)) {
                isSuccess = false;
                result = "" + mContext.getResources().getString(R.string.check_internet);
                return result;
            }
            params[0] = params[0].replace(" ", "%20");
//            String url = CommonData.BasePath + "lang=" + SessionSave.getSession("Lang", mContext) + "&" + params[0];
            String url = CommonData.BasePath + params[0];
            Log.d("Api request", "" + url + "\ndata" + data);
            System.out.println("Api request: " + url);
            System.out.println("Api request" + "data :" + data);
            if (GetMethod) {
                requestingMethod = 0;
            } else {
                requestingMethod = 1;
            }
            if (requestingMethod == 1) {
                JOR = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jresponse) {
                        try {
                            if (volleyProgressBarDialog != null || volleyProgressBarDialog.isShowing()) {
                                volleyProgressBarDialog.dismiss();
                                volleyProgressBarDialog = null;
                            }

                            response.getResult(isSuccess, jresponse.toString());
                            result = jresponse.toString();
                            System.out.println("Driver Api response" + result);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (volleyProgressBarDialog != null || volleyProgressBarDialog.isShowing()) {
                                volleyProgressBarDialog.dismiss();
                                volleyProgressBarDialog = null;
                            }
                            String errorMessage = "Please Try Again, ";
                            if (error instanceof NetworkError) {
                                errorMessage += "Network Connection Error ";
                            } else if (error instanceof ServerError) {
                                errorMessage += "Server connection Error ";
                            } else if (error instanceof AuthFailureError) {
                                errorMessage += "AuthFailureError ";
                            } else if (error instanceof ParseError) {
                                errorMessage += "ParseError ";
                            } else if (error instanceof NoConnectionError) {
                                errorMessage += "Network Connection Error ";
                            } else if (error instanceof TimeoutError) {
                                errorMessage += "Request Timeout";
                            }
                            response.getResult(false, errorMessage + " error:" + error);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<String, String>();
                        if (!GetMethod) {
                            param.put("Content-Type", "application/x-www-form-urlencodred;charset=UTF-8");
                            return param;
                        }
                        return param;
                    }
                };
                JOR.setRetryPolicy(new DefaultRetryPolicy(15000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //JOR.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(JOR);

            } else {

                JOR = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject jresponse) {
                                // display response
                                try {
                                    if (volleyProgressBarDialog != null || volleyProgressBarDialog.isShowing()) {
                                        volleyProgressBarDialog.dismiss();
                                        volleyProgressBarDialog = null;
                                    }

                                    response.getResult(isSuccess, jresponse.toString());
                                    result = jresponse.toString();
                                    System.out.println("Driver Api response" + result);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", response.toString());
                            }
                        }
                );
                JOR.setRetryPolicy(new DefaultRetryPolicy(15000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //JOR.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(JOR);

            }

            //JOR.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}