
package com.embedded.controlemultimidiauniversal.net;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.equipment.EquipmentType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;

import java.security.InvalidParameterException;
import java.util.Map;

public class HttpSenderTask extends AsyncTask<String, Void, String> {

    /**
     * M&eacute;todo que gera uma URL v&aacute;lida apartir de um Map.
     * 
     * @param params Um Map com as seguintes chaves, sendo que a chave "address"
     *            &eacute; obrigat&oacute;ria:
     *            <ul>
     *            <li><b>address</b> - O endere&ccedil;o;</li>
     *            <li><b>path</b> - Caminho dentro do endere&ccedil;o.</li>
     *            </ul>
     * @return Uma URL apartir dos dados informados.
     * @throws InvalidParameterException Se alguma chave for omitida no Map.
     */
    public static String createURL(Map<String, String> params) throws InvalidParameterException {

        if (params.containsKey("address")) {
            Log.d(MainActivity.TAG, "Erro");
            Log.d(MainActivity.TAG, params.get("address"));
            Log.d(MainActivity.TAG, "Erro");
            Builder uriBuilder = Uri.parse(params.get("address")).buildUpon();
            uriBuilder.scheme("http");
            params.remove("address");

            if (params.containsKey("path")) {
                uriBuilder.path(params.get("path"));
                params.remove("path");

                if (params.size() > 0) {
                    for (Map.Entry<String, String> e : params.entrySet())
                        uriBuilder.appendQueryParameter(e.getKey().toString(), e.getValue()
                                .toString());
                }
                Log.i("info", uriBuilder.toString());
            }
            return uriBuilder.toString();
        } else {
            throw new InvalidParameterException("Parametro url ausente");
        }
    }

    /**
     * M&eacute;todo que gera uma URL v&aacute;lida apartir de um Map e
     * informa&ccedil;&otilde;es do c&ocirc;modo a ser controloado.
     * 
     * @param params Um Map com as seguintes chaves, sendo que a chave "address"
     *            &eacute; obrigat&oacute;ria:
     *            <ul>
     *            <li><b>address</b> - O endere&ccedil;o;</li>
     *            <li><b>path</b> - Caminho dentro do endere&ccedil;o.</li>
     *            </ul>
     * @param nameRoom Nome do c&ocirc;modo.
     * @param equipment Equipmento a ser controlado.
     * @param command Comando a ser enviado.
     * @return Uma URL apartir dos dados informados.
     * @throws InvalidParameterException Se alguma chave for omitida no Map.
     */
    public static String createURL(Map<String, String> params, String nameRoom,
            EquipmentType equipment, Command command) throws InvalidParameterException {

        params.put("nameRoom", nameRoom);
        params.put("equipment", equipment.toString());
        params.put("command", command.toString());

        return createURL(params);
    }

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
                    httpClientConnection = new HttpClientConnection(url, HttpProtocol.GET);
                    response = httpClientConnection.execute();
                } else if (method.equals("post")) {
                    httpClientConnection = new HttpClientConnection(url, HttpProtocol.POST);
                    response = httpClientConnection.execute();
                }

                int statusCode = NetUtils.getHttpResponse(response);
                if (statusCode == HttpStatus.SC_OK) {
                    try {
                        return NetUtils.readResponse(response);
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
