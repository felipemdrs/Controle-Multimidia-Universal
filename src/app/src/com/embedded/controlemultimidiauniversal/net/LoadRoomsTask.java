package com.embedded.controlemultimidiauniversal.net;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;
import com.embedded.controlemultimidiauniversal.equipment.Equipment;
import com.embedded.controlemultimidiauniversal.net.http.HttpClientConnection;
import com.embedded.controlemultimidiauniversal.net.http.HttpProtocol;
import com.embedded.controlemultimidiauniversal.net.http.HttpUtils;
import com.embedded.controlemultimidiauniversal.net.http.TimeOut;
import com.embedded.controlemultimidiauniversal.room.Room;
import com.embedded.controlemultimidiauniversal.room.adapter.RoomAdapter;
import com.example.aulatabview.R;

//Params, Progress, Result
public class LoadRoomsTask extends AsyncTask<String, String, String> {

	private Context myContext;
	private RoomAdapter mAdapter;
	private List<Room> listRooms;
	private final String RESULT_OK = "OK";
	private final String RESULT_FAIL = "NO DATA";

	// private ProgressDialog myProgressDialog;

	public LoadRoomsTask(Context context, RoomAdapter adapter,
			List<Room> newListRooms) {
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

		try {
			ProgressBar prog = (ProgressBar) ((Activity) myContext)
					.findViewById(R.id.progressBar1);
			TextView msgLoad = (TextView) ((Activity) myContext)
					.findViewById(R.id.textView1);
			TextView logLoadRoom = (TextView) ((Activity) myContext)
					.findViewById(R.id.logLoadRoom);
			msgLoad.setVisibility(View.GONE);
			prog.setVisibility(View.GONE);
			logLoadRoom.setVisibility((result.equals("OK") ? View.GONE
					: View.VISIBLE));
		} catch (Exception e) {
		}
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {
		String actualNameRoom, resultOperation, url, resp;
		HttpClientConnection httpGet;
		HttpResponse result;

		actualNameRoom = ((INamedRoom) myContext).getNameRoom();
		resultOperation = null;
		url = params[0];

		httpGet = new HttpClientConnection(url, HttpProtocol.GET,
				TimeOut.VERY_BIG);
		result = httpGet.execute();

		int statusCode = HttpUtils.getHttpResponse(result);

		if (statusCode == HttpStatus.SC_OK) {
			resp = HttpUtils.readResponse(result);

			listRooms.clear();

			Log.d(MainActivity.TAG, "resp: " + resp);

			if (resp != null && !resp.isEmpty()) {

				resp = resp.substring(1, resp.length() - 1).replace("\\", "");
				Log.d(MainActivity.TAG, resp);
				readJSON(actualNameRoom, resp);
				resultOperation = RESULT_OK;
			} else
				resultOperation = RESULT_FAIL;
		}
		return resultOperation;
	}

	private void readJSON(String actualNameRoom, String resp) {
		JSONObject mainObject;
		try {
			mainObject = new JSONObject(resp);
			JSONArray roomsArrays = mainObject.getJSONArray("rooms");
			for (int i = 0; i < roomsArrays.length(); i++) {
				String nameRoom = roomsArrays.getJSONObject(i)
						.getString("room");
				if (!nameRoom.equals(actualNameRoom.toLowerCase())) {
					Room roomTemp = new Room(nameRoom);

					JSONArray equipmentsArrays = roomsArrays.getJSONObject(i)
							.getJSONArray("equipments");
					for (int j = 0; j < equipmentsArrays.length(); j++) {
						String nameEquipment = equipmentsArrays
								.getJSONObject(j).getString("equipment");
						Boolean statusPower = equipmentsArrays.getJSONObject(j)
								.getBoolean("status");
						Integer channel = equipmentsArrays.getJSONObject(j)
								.getInt("channel");
						Integer volume = equipmentsArrays.getJSONObject(j)
								.getInt("volume");
						roomTemp.addEquipment(new Equipment(nameEquipment,
								channel, volume, statusPower));

					}
					listRooms.add(roomTemp);
				}
			}

		} catch (JSONException e1) {
			Log.d(MainActivity.TAG, "erro " + e1.getMessage());
		}
	}

}
