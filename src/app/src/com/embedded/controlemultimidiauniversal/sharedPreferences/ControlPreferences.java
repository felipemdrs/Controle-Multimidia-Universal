package com.embedded.controlemultimidiauniversal.sharedPreferences;

import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class ControlPreferences {
	private final String PREFS_NAME = "Preferences_Control";

	public final static String PREFS_NAME_ROOMS = "nameRooms";
	public final static String PREFS_RESIDENCE_ADDRESS = "residenceAddress";

	private Activity activity;

	public ControlPreferences(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Método para recuperar as preferências do controlle.
	 * 
	 * @return Mapa de strings contendo as preferências.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPreferences() {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,
				0);
		return (Map<String, String>) settings.getAll();
	}

	public void setPreferences(WeightStack weightStackNameRooms,
			WeightStack weightStackResidenceAddress) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,
				0);

		SharedPreferences.Editor editor = settings.edit();

		if (weightStackNameRooms != null)
			editor.putString(PREFS_NAME_ROOMS, weightStackNameRooms.toString());
		if (weightStackResidenceAddress != null)
			editor.putString(PREFS_RESIDENCE_ADDRESS,
					weightStackResidenceAddress.toString());
		editor.commit();
	}

	public void setNamePreference(WeightStack weightStackNameRooms) {
		Log.d("ControlPreferences",
				"Address saved " + weightStackNameRooms.toString());
		setPreferences(weightStackNameRooms, null);
	}

	public void setAddressPreference(WeightStack weightStackResidenceAddress) {
		Log.d("ControlPreferences", "Address saved "
				+ weightStackResidenceAddress.toString());
		setPreferences(null, weightStackResidenceAddress);
	}
}
