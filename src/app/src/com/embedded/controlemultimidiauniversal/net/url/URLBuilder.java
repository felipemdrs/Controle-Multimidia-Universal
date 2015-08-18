package com.embedded.controlemultimidiauniversal.net.url;

import java.security.InvalidParameterException;
import java.util.Map;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import com.embedded.controlemultimidiauniversal.equipment.Command;
import com.embedded.controlemultimidiauniversal.equipment.EquipmentType;

public class URLBuilder {
	
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

        if (params.containsKey(UrlParams.ADDRESS)) {
            Builder uriBuilder = Uri.parse(params.get(UrlParams.ADDRESS)).buildUpon();
            uriBuilder.scheme("http");
            params.remove(UrlParams.ADDRESS);

            if (params.containsKey(UrlParams.PATH)) {
                uriBuilder.path(params.get(UrlParams.PATH));
                params.remove(UrlParams.PATH);

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

        params.put(UrlParams.NAME_ROOM, nameRoom);
        params.put(UrlParams.EQUIPMENT, equipment.toString());
        params.put(UrlParams.COMMAND, command.toString());

        return createURL(params);
    }
}
