package com.embedded.controlemultimidiauniversal.net;

import com.embedded.controlemultimidiauniversal.MainActivity;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetUtils {

	private static final int STATUS_FAILED = -1;

	/**
	 * Return the HttpStatus of the network request
	 * 
	 * @param response
	 *            The HttpResponse object
	 * @return The HttpStatus code
	 */
	public static int getHttpResponse(HttpResponse response) {
		int status = STATUS_FAILED;
		if (response != null && response.getStatusLine() != null) {
			status = response.getStatusLine().getStatusCode();
		}
		return status;
	}

	public static String readResponse(HttpResponse response) {
		BufferedReader rd;
		String webServiceInfo = "";
		String message = "";
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			while ((webServiceInfo = rd.readLine()) != null) {
				Log.d("info", webServiceInfo);
				message += webServiceInfo;
			}
		} catch (Exception e) {
			if (MainActivity.D)
				Log.d(MainActivity.TAG, e.getMessage());
		}

		return message;
	}

	public static boolean hasConnection(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}
}
