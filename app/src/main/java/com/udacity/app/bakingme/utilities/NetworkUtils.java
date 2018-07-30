package com.udacity.app.bakingme.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Created by E560XT on 7/24/2018. */
public class NetworkUtils {

  public static final String URL =
      "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

  public static boolean isOnline(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}
