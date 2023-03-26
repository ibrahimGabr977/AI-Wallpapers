package com.hope.igb.aiwallpapers.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;



public final class CheckNetworkState {


    public static boolean isInternetAvailable(Context context){


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

            if (capabilities != null)
                return  capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

        }
        // For below 29 api
        else {
            if (connectivityManager.getActiveNetworkInfo() != null)
                return  connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        }

        return false;
    }







}
