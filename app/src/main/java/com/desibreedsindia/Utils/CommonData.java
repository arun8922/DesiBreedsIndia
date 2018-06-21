package com.desibreedsindia.Utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Class for common variable which used in entire project.

/**
 * Developer 24.11.2016
 */
public class CommonData {

//    public static String BasePath = "http://thangamlodge.in/foodkourt_api/mobile_api.php/Dr9HfwAcb0GK49zXxtuW0w==?type=";

    public static String BasePath = "http://test.logicmaker.co.in/api/api.php?api_name=";
    public static String companykey = "RH7PVsKE18qGe6Y6YOC8kdRntOEyDnD0uW";

    public static int ch = 0;
    public static int RegistrationPhase = 1;
    public static String mDevice_id;
    public static String petrol = "1";
    public static String food = "2";
    public static String rent = "3";
    public static String misc = "4";
    public static String flaggertRide = "0";
    public static String streetRide = "2";
    public static String BtnStr = "Start Waiting";
    public static String BtnTag = "start";
    public static String ArrivedBtnTag = "Arrived";
    public static double getlatitude = 0.0;
    public static double getlongitude = 0.0;
    public static double last_getlatitude = 0.0;
    public static double last_getlongitude = 0.0;
    public static double travel_km = 0.0;
    public static String hstravel_km = "";
    public static String current_act;
    public static String currentspeed = "";
    public static boolean iswaitingrunning;
    public static boolean speed_waiting_stop = true;
    public static String below_speed = "";
    public static int km_calc = 0;
    public static Context sContext;
    public static List<HashMap<String, String>> countryDetails = new ArrayList<HashMap<String, String>>();
    public static List<String> Regcompanylist = new ArrayList<>();
    public static String defaultCountryCode;
    public static String countrycodelist[];
    public static String CountryCodeList[];
    public static ArrayList<String> onArrayup = new ArrayList<String>();
    public static ArrayList<String> onArrayavoid = new ArrayList<String>();
    public static String GCM_Token = "";

    public static int destroyWaitingTime = 0;
    // Fare calculation for Fare review
    public static String FareReviewreason = "";
    public static boolean FareReviewSkippedByDriver = false;

    public static String encodeToBase64() {
        String live_key = "ntaxi_" + companykey;
        String encodedString = "";
        try {
            byte[] byteData = null;
            if (Build.VERSION.SDK_INT >= 8) {
                byteData = android.util.Base64.encode(live_key.getBytes(), android.util.Base64.DEFAULT);
            }
            encodedString = new String(byteData);
        } catch (Exception e) {
            Log.i("Exception encode base64", "" + e.toString());
        }
        try {
            encodedString = URLEncoder.encode(encodedString, "utf-8");
            encodedString = encodedString.replace("%0A", "");
        } catch (UnsupportedEncodingException e) {
            Log.i("ConversionNotSupported", "" + e.toString());
        }
        return encodedString;
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

    /*
     * method to clear the cache. input-context
     */
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

}