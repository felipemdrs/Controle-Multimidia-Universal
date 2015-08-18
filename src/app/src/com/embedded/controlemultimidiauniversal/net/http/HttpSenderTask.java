package com.embedded.controlemultimidiauniversal.net.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask para o envio requisi&ccedil;&atilde;o via HTTP para o servidor.
 * 
 */
public class HttpSenderTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		try {

			if (params.length == 2) {
				HttpClientConnection httpClientConnection;
				HttpResponse response = null;

				String method = params[0];
				String url = params[1];

				Log.i("info", url);
				if (method.equals("get")) {
					httpClientConnection = new HttpClientConnection(url,
							HttpProtocol.GET, TimeOut.MIDDLE);
					response = httpClientConnection.execute();
				} else if (method.equals("post")) {
					httpClientConnection = new HttpClientConnection(url,
							HttpProtocol.POST, TimeOut.MIDDLE);
					response = httpClientConnection.execute();
				}

				int statusCode = HttpUtils.getHttpResponse(response);
				if (statusCode == HttpStatus.SC_OK) {
					try {
						return HttpUtils.readResponse(response);
					} catch (Exception e) {
						return null;
					}
				}
			}
		} catch (Exception e) {
			Log.e("info", e.getMessage());
			return e.getMessage();
		}
		return null;
	}
}
