package com.embedded.controlemultimidiauniversal.room;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.net.http.HttpSenderTask;
import com.embedded.controlemultimidiauniversal.net.url.URLBuilder;
import com.example.aulatabview.R;

public class OptionsCopyConfiguration extends Dialog {

	private String nameRoomOfCopy;
	private Context mContext;

	public OptionsCopyConfiguration(Context context, String nameRoomOfCopy) {
		super(context);
		this.nameRoomOfCopy = nameRoomOfCopy;
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_options_copy_configuration);
		clickListenerButtonCancel();
		clickListenerButtonCopy();
	}

	public void clickListenerButtonCancel() {
		ImageButton buttonCancel = (ImageButton) findViewById(R.id.buttonCancelConfiguration);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void clickListenerButtonCopy() {
		ImageButton buttonCopy = (ImageButton) findViewById(R.id.buttonCopyConfiguration);
		buttonCopy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String canCopyTv, canCopySound;
				CheckBox checkCopyTv = (CheckBox) findViewById(R.id.cbCopyTv);
				CheckBox checkCopySound = (CheckBox) findViewById(R.id.cbCopySound);

				canCopyTv = String.valueOf(checkCopyTv.isChecked());
				canCopySound = String.valueOf(checkCopySound.isChecked());
				
				Log.d(MainActivity.TAG, checkCopySound.toString());

				MainActivity act = (MainActivity) mContext;

				Map<String, String> params = new HashMap<String, String>();

				params.put("address", act.getResidenceAddress());
				params.put("path", "copyConfigurationsOfRoom");

				params.put("copyOfRoom", nameRoomOfCopy);
				params.put("pasteInRoom", act.getNameRoom());
				params.put("canCopyTv", canCopyTv);
				params.put("canCopySound", canCopySound);

				String url = URLBuilder.createURL(params);
				new HttpSenderTask().execute("get", url);

				// String url = iResidenceAddress.getResidenceAddress()
				// + "/copyConfigurationsOfRoom?copyOfRoom=" + nameRoomOfCopy
				// + "&&pasteInRoom="+ iNamedRoom.getNameRoom()+"&&canCopyTv=" +
				// canCopyTv + "&&canCopySound="
				// + canCopySound;
				Log.d(MainActivity.TAG, url);

				// HttpClientConnection httpGet = new HttpClientConnection(url,
				// HttpProtocol.GET);
				// HttpResponse result = httpGet.execute();
				// String resp = NetUtils.readResponse(result);
				// if (resp != null && !resp.equals("")) {
				// Log.d(MainActivity.TAG, resp);
				// }
				dismiss();
			}
		});
	}
}
