
package com.embedded.controlemultimidiauniversal.net;

import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.equipment.Equipment;
import com.embedded.controlemultimidiauniversal.room.Room;
import com.embedded.controlemultimidiauniversal.room.adapter.RoomAdapter;
import com.example.aulatabview.R;

//Params, Progress, Result
public class LoadRooms extends AsyncTask<String, String, String> {

    private Context myContext;

    private RoomAdapter mAdapter;

    private List<Room> listRooms;

    // private ProgressDialog myProgressDialog;

    public LoadRooms(Context context, RoomAdapter adapter, List<Room> newListRooms) {
        this.myContext = context;
        this.mAdapter = adapter;
        this.listRooms = newListRooms;

    }

    @Override
    protected void onPreExecute() {
        // myProgressDialog = ProgressDialog.show(classListRooms.getContext(),
        // "Carregando Cômodos",
        // "Aguarde equanto os cômodos são carregados");

        Log.d(MainActivity.TAG, "pre execute");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {

        mAdapter.setList(listRooms);
        mAdapter.notifyDataSetChanged();

        if ((result.equals("OK"))) {
            try {
                ProgressBar prog = (ProgressBar)((Activity)myContext)
                        .findViewById(R.id.progressBar1);
                TextView msgLoad = (TextView)((Activity)myContext).findViewById(R.id.textView1);
                TextView logLoadRoom = (TextView)((Activity)myContext).findViewById(R.id.logLoadRoom);
                msgLoad.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                logLoadRoom.setVisibility(View.GONE);
            } catch (Exception e) {
                // TODO: handle exception
            }

        } else {
            try {
                ProgressBar prog = (ProgressBar)((Activity)myContext)
                        .findViewById(R.id.progressBar1);
                TextView msgLoad = (TextView)((Activity)myContext).findViewById(R.id.textView1);
                TextView logLoadRoom = (TextView)((Activity)myContext).findViewById(R.id.logLoadRoom);
                msgLoad.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                logLoadRoom.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {

        String resultOperation = null;

        listRooms.clear();
        JSONObject mainObject;

        String url = params[0];
        HttpClientConnection httpGet = new HttpClientConnection(url, HttpProtocol.GET);
        HttpResponse result = httpGet.execute();
        String resp = NetUtils.readResponse(result);

        Log.d(MainActivity.TAG, "resp: " + resp);
        if (resp != null && !resp.equals("")) {

            resp = resp.substring(1, resp.length() - 1).replace("\\", "");
            Log.d(MainActivity.TAG, resp);

            try {
                mainObject = new JSONObject(resp);
                JSONArray roomsArrays = mainObject.getJSONArray("rooms");
                for (int i = 0; i < roomsArrays.length(); i++) {
                    String nameRoom = roomsArrays.getJSONObject(i).getString("room");
                    Room roomTemp = new Room(nameRoom);

                    JSONArray equipmentsArrays = roomsArrays.getJSONObject(i).getJSONArray(
                            "equipments");
                    for (int j = 0; j < equipmentsArrays.length(); j++) {
                        String nameEquipment = equipmentsArrays.getJSONObject(j).getString(
                                "equipment");
                        Boolean statusPower = equipmentsArrays.getJSONObject(j)
                                .getBoolean("status");
                        Integer channel = equipmentsArrays.getJSONObject(j).getInt("channel");
                        Integer volume = equipmentsArrays.getJSONObject(j).getInt("volume");

                        roomTemp.addEquipment(new Equipment(nameEquipment, channel, volume,
                                statusPower));

                    }
                    listRooms.add(roomTemp);
                }

            } catch (JSONException e1) {
                Log.d(MainActivity.TAG, "erro " + e1.getMessage());
                e1.printStackTrace();
            }
            resultOperation = "OK";
        } else {
            resultOperation = "NO DATA";
        }

        return resultOperation;
    }

}
