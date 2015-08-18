package com.embedded.controlemultimidiauniversal.net.http;

import com.embedded.controlemultimidiauniversal.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import java.io.IOException;

/**
 * Classe para enviar requisi&ccedil;&otilde;es para uma determinado url do
 * servidor.
 * 
 */
public class HttpClientConnection {
	private HttpClient mHttpClient;

	private HttpGet mGet;

	private HttpPost mPost;

	/**
	 * Construtor da classe.
	 * 
	 * @param url
	 *            URL destinat&aacute;ria para envio da
	 *            requisi&ccedil;&atilde;o.
	 * @param httpProtocol
	 *            O m&eacute;todo HTTP de envio.
	 * @param timeOut
	 *            Tempo limite da opera&ccedil;&atilde;o.
	 */
	public HttpClientConnection(String url, HttpProtocol httpProtocol,
			TimeOut timeOut) {

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeOut.getValue());
		HttpConnectionParams.setSoTimeout(httpParameters, timeOut.getValue());

		mHttpClient = new DefaultHttpClient(httpParameters);

		switch (httpProtocol) {
		case GET:
			mGet = new HttpGet(url);
			break;

		case POST:
			mPost = new HttpPost(url);
			break;
		}

	}

	/**
	 * Envia uma requisa&ccedil;&atilde;o HTTP do tipo GET ou POST.
	 * 
	 * @return Resposta do servidor caso a requisi&ccedil;&atilde;o seja
	 *         conclu&iacute;da.
	 */
	public HttpResponse execute() {
		try {
			if (mGet != null) {
				return mHttpClient.execute(mGet);
			} else if (mPost != null) {
				return mHttpClient.execute(mPost);
			}
		} catch (ClientProtocolException e) {
			if (MainActivity.D)
				Log.d(getClass().getSimpleName(), "ClientProtocolException");
			return null;
		} catch (IOException e) {
			if (MainActivity.D)
				Log.d(getClass().getSimpleName(), "IOException");
			return null;
		}
		return null;
	}
}
