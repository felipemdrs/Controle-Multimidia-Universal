package com.embedded.controlemultimidiauniversal.net.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.example.aulatabview.R;

/**
 * Classe que disp&otilde;e diversos m&eacute;todos utilit&aacute;rios para
 * conex&atilde;o por HTTP.
 * 
 * @author Felipe
 * 
 */
public class HttpUtils {

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

	/**
	 * Ler uma resposta do servidor.
	 * 
	 * @param response
	 *            Um HttpRespose v&aacute;lido recebido do servidor.
	 * @return Uma String com a resposta.
	 */
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

	/**
	 * Verifica se h&aacute; conex&atilde;o no dispositivo.
	 * 
	 * @param context
	 *            Contexto principal da aplica&ccedil;&atilde;o.
	 * @return True se tiver conex&atilde;o e False caso contr&aacute;rio.
	 */
	public static boolean hasConnection(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}

	/**
	 * Exibe uma menssagem na tela informando que n&atilde;o h&aacute;
	 * conex&atilde;o dispon&iacute;vel.
	 * 
	 * @param context
	 *            Contexto principal da aplica&ccedil;&atilde;o.
	 */
	public static void showConnectionFail(final Context context) {
		/*Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(context.getString(R.string.wifi_disable));
		builder.setPositiveButton(context.getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						((MainActivity) context).finish();
					}
				});

		builder.show();*/
	}
}
