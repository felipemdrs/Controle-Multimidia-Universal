
package com.embedded.controlemultimidiauniversal.room.adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import android.os.AsyncTask;
import android.util.Log;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;
import com.embedded.controlemultimidiauniversal.net.http.HttpClientConnection;
import com.embedded.controlemultimidiauniversal.net.http.HttpProtocol;
import com.embedded.controlemultimidiauniversal.net.http.HttpSenderTask;
import com.embedded.controlemultimidiauniversal.net.http.HttpUtils;
import com.embedded.controlemultimidiauniversal.net.http.TimeOut;
import com.embedded.controlemultimidiauniversal.net.url.URLBuilder;
import com.embedded.controlemultimidiauniversal.sharedPreferences.WeightStack;

public class VerifiyNameRoomTask extends AsyncTask<Void, Void, String> {

    private final WeightStack weightStackNameRooms;

    private final INamedRoom iNamedRoom;

    private final String residenceAddress;

    public VerifiyNameRoomTask(INamedRoom iNamedRoom, WeightStack weightStackNameRooms,
            String address) {
        this.weightStackNameRooms = weightStackNameRooms;
        this.iNamedRoom = iNamedRoom;
        this.residenceAddress = address;
    }

    @Override
    protected String doInBackground(Void... param) {

        String url, resp;

        for (String nameRoom : weightStackNameRooms.getAll()) {
            if (nameRoom != null) {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("VerifyNameRoom", "Room: " + nameRoom);

                params.put("address", residenceAddress);
                params.put("path", "roomIsActive");
                params.put("nameRoom", nameRoom);

                Log.d("VerifyNameRoom", "Adrress: " + residenceAddress);
                url = URLBuilder.createURL(params);

                HttpClientConnection httpClientConnection;
                HttpResponse response = null;

                httpClientConnection = new HttpClientConnection(url, HttpProtocol.GET, TimeOut.BIG);
                response = httpClientConnection.execute();
                Log.d(MainActivity.TAG, residenceAddress);
                int statusCode = HttpUtils.getHttpResponse(response);
                if (statusCode == HttpStatus.SC_OK) {
                    try {
                        Log.d("VerifyNameRoom", "verificandoooooooooooooO");
                        resp = HttpUtils.readResponse(response);
                        Log.d("VerifyNameRoom", "Response: " + resp);
                        if (resp.equals("True"))
                            return nameRoom;
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.d("Info", "Achouu " + result);
            iNamedRoom.setNameRoom(result);
        }
        super.onPostExecute(result);
    }

}
