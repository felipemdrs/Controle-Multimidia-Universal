package com.embedded.controlemultimidiauniversal.control;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.net.NetUtils;
import com.embedded.controlemultimidiauniversal.net.SearchResidence;
import com.example.aulatabview.R;

public class ControlStart {

	private Context mContext;

	public ControlStart(Context context) {
		this.mContext = context;
/*
		if (NetUtils.hasConnection(context) && MainActivity.RUN_IN_DEVICE) {
			callSearchResidence();
		} else {
			Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setMessage(mContext.getString(R.string.wifi_disable));
			builder.setPositiveButton(mContext.getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							((MainActivity) mContext).finish();
						}
					});

			builder.show();
		}*/

	}

	public void callSearchResidence() {
		SearchResidence searchResidence = new SearchResidence(mContext);
		searchResidence.execute();
	}
}
