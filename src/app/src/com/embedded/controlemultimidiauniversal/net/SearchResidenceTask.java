package com.embedded.controlemultimidiauniversal.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.conn.util.InetAddressUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.Interface.IResidenceAddress;
import com.embedded.controlemultimidiauniversal.net.http.HttpClientConnection;
import com.embedded.controlemultimidiauniversal.net.http.HttpProtocol;
import com.embedded.controlemultimidiauniversal.net.http.HttpUtils;
import com.embedded.controlemultimidiauniversal.net.http.TimeOut;
import com.embedded.controlemultimidiauniversal.net.url.UrlParams;
import com.embedded.controlemultimidiauniversal.net.url.URLBuilder;
import com.embedded.controlemultimidiauniversal.sharedPreferences.WeightStack;
import com.example.aulatabview.R;

public class SearchResidenceTask extends AsyncTask<Void, Integer, String> {

	private ProgressDialog pd = null;

	private final String DEFAULT_IP = "192.168.2.5";

	private Context mContext;

	private final int MAX_SEARCH = 4;

	private final int MAX_RANGE = 11;

	private WeightStack weightStackResidenceAddresses;

	public SearchResidenceTask(Context activityContextApplication) {
		SharedPreferences settings = ((MainActivity) activityContextApplication)
				.getSharedPreferences("Preferences_Control", 0);

		Log.d(MainActivity.TAG,
				(activityContextApplication == null ? "eh null diferenciado"
						: "nao eh null diferenceas"));
		this.mContext = activityContextApplication;
		weightStackResidenceAddresses = new WeightStack();
		String ipSharedPreference = (String) settings.getAll().get(
				"residenceAddress");
		if (ipSharedPreference != null)
			weightStackResidenceAddresses.create(ipSharedPreference);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int progress = (100 * values[0]) / (MAX_RANGE * MAX_SEARCH);
		pd.setProgress(progress);
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(mContext);
		pd.setTitle(mContext.getString(R.string.progress_dialog_searchTitle));
		pd.setMessage(mContext.getString(R.string.progress_dialog_searchText));
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		pd.show();
		super.onPreExecute();
	}

	private String getIPv4() {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (isIPv4)
							return sAddr;
					}
				}
			}
		} catch (Exception ex) {
		}
		return "";
	}

	private String getFormatedIPv4() {
		String ipV4 = getIPv4();
		String[] ipSplited = ipV4.split("\\.");
		return ipSplited[0] + "." + ipSplited[1] + ".";
	}

	@Override
	protected String doInBackground(Void... params) {
		String address = verifyWeighStackResidenceAddress();

		verifyWeighStackResidenceAddress();

		if (address != null) {
			return address;
		} else {
			String ipV4 = (!MainActivity.D ? getFormatedIPv4() : DEFAULT_IP);
			int rangeIpSearched = 0;

			try {
				for (int i = 0; i < MAX_SEARCH; i++) {
					for (int j = 0; j < MAX_RANGE; j++) {
						publishProgress(rangeIpSearched++);
						address = searchAddress("http://" + ipV4
								+ String.valueOf(i) + "." + String.valueOf(j)
								+ ":5432", TimeOut.SMALL);
						if (address != null)
							return address;
					}
				}

			} catch (Exception e) {
				Log.e("info", e.getMessage());
				return null;
			}
		}
		return null;
	}

	private String searchAddress(String address, TimeOut timeOut) {
		try {
			Map<String, String> param = new HashMap<String, String>();

			param.put(UrlParams.ADDRESS, address);
			address = URLBuilder.createURL(param);

			HttpClientConnection httpClientConnection;
			HttpResponse response = null;
			Log.d("Info", "Redisidence search " + address);
			httpClientConnection = new HttpClientConnection(address,
					HttpProtocol.GET, timeOut);
			response = httpClientConnection.execute();
			Log.d(MainActivity.TAG, address);
			int statusCode = HttpUtils.getHttpResponse(response);
			if (statusCode == HttpStatus.SC_OK) {
				try {
					String message = HttpUtils.readResponse(response);
					if (message
							.equals("Welcome to Control multimedia Universal!")) {
						return address;
					}
				} catch (Exception e) {
					return null;
				}
			}
		} catch (Exception e) {
			Log.e("Info", "Search Residence " + e.getMessage());
		}
		return null;
	}

	private String verifyWeighStackResidenceAddress() {
		for (String address : weightStackResidenceAddresses.getAll()) {
			address = searchAddress(address, TimeOut.VERY_BIG);
			if (address != null)
				return address;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		pd.dismiss();
		if (result != null) {
			((IResidenceAddress) mContext).setResidenceAddress(result);
		} else {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					mContext);
			builder.setMessage(
					mContext.getString(R.string.confirm_dialog_searchTitle))
					.setPositiveButton(
							mContext.getString(R.string.confirm_dialog_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									builder.create();
									onCancelled();
									new SearchResidenceTask(mContext).execute();
								}
							})
					.setNegativeButton(
							mContext.getString(R.string.confirm_dialog_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((MainActivity) mContext).finish();
								}
							});
			builder.setCancelable(false);
			builder.show();
		}
	}
}
