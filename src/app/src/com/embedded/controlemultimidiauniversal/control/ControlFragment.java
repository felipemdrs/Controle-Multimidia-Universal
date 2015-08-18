package com.embedded.controlemultimidiauniversal.control;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.embedded.controlemultimidiauniversal.MainActivity;
import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;
import com.embedded.controlemultimidiauniversal.Interface.IResidenceAddress;
import com.embedded.controlemultimidiauniversal.bluetooth.BluetoothService;
import com.embedded.controlemultimidiauniversal.bluetooth.BluetoothServiceReceiver;
import com.embedded.controlemultimidiauniversal.equipment.Command;
import com.embedded.controlemultimidiauniversal.equipment.EquipmentType;
import com.embedded.controlemultimidiauniversal.net.http.HttpSenderTask;
import com.embedded.controlemultimidiauniversal.net.url.URLBuilder;
import com.embedded.controlemultimidiauniversal.room.adapter.VerifiyNameRoomTask;
import com.embedded.controlemultimidiauniversal.sharedPreferences.ControlPreferences;
import com.embedded.controlemultimidiauniversal.sharedPreferences.WeightStack;
import com.embedded.controlemultimidiauniversal.sleep.CustomNumberPick;
import com.example.aulatabview.R;

public class ControlFragment extends Fragment implements INamedRoom,
		IResidenceAddress {

	private EquipmentType equipment = EquipmentType.TV;

	private String address;

	private String nameRoom = "Teste";

	private MainActivity mainActivity;

	private Activity activity;

	private BluetoothServiceReceiver bluetoothServiceReceive;

	private ControlPreferences controlPreferences;

	private TextView txt_nameRoom;

	private WeightStack weightStackNameRooms = new WeightStack();

	private WeightStack weightStackResidenceAddress = new WeightStack();

	public ControlFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	private void getSharedPreference() {
		controlPreferences = new ControlPreferences(mainActivity);
		Map<String, String> preferences = controlPreferences.getPreferences();

		String sharedPreferenceAddress = preferences
				.get(ControlPreferences.PREFS_NAME_ROOMS);
		String sharedPreferenceNameRooms = preferences
				.get(ControlPreferences.PREFS_RESIDENCE_ADDRESS);

		if (sharedPreferenceAddress != null)
			weightStackResidenceAddress.create(sharedPreferenceAddress);
		if (sharedPreferenceNameRooms != null)
			weightStackNameRooms.create(sharedPreferenceNameRooms);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = super.getActivity();
		View view = inflater.inflate(R.layout.control, container, false);
		createBluetoothServiceIntent();
		createRadioGroupEvent(view);
		createButtonsEvent(view);
		return view;
	}

	@Override
	public void onStart() {
		txt_nameRoom = (TextView) activity.findViewById(R.id.textViewNameRoom);
		getSharedPreference();
		super.onStart();
	}

	private void createBluetoothServiceIntent() {
		IntentFilter filter = new IntentFilter(
				BluetoothServiceReceiver.BLUETOOTH_RESULT);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		bluetoothServiceReceive = new BluetoothServiceReceiver(this);
		activity.registerReceiver(bluetoothServiceReceive, filter);

		Intent bluetoothIntent = new Intent(activity, BluetoothService.class);
		activity.startService(bluetoothIntent);
	}

	@Override
	public void setNameRoom(String nameRoom) {
		if (nameRoom != null) {
			this.nameRoom = nameRoom;
			txt_nameRoom.setText(this.nameRoom);
			weightStackNameRooms.add(nameRoom);
			controlPreferences.setNamePreference(weightStackNameRooms);
			Log.d("Info", "setAdd " + weightStackNameRooms.toString());
		}
	}

	@Override
	public String getNameRoom() {
		return nameRoom;
	}

	@Override
	public void setResidenceAddress(String residenceAddress) {
		if (residenceAddress != null) {
			address = residenceAddress;
			weightStackResidenceAddress.add(residenceAddress);

			controlPreferences
					.setAddressPreference(weightStackResidenceAddress);

			VerifiyNameRoomTask verifiyNameRoomTask = new VerifiyNameRoomTask(
					this, weightStackNameRooms, residenceAddress);
			verifiyNameRoomTask.execute();
		}
	}

	@Override
	public String getResidenceAddress() {
		return address;
	}

	private void createRadioGroupEvent(View view) {
		RadioGroup equipmentGroup = (RadioGroup) view
				.findViewById(R.id.radioGroupEquipments);
		equipmentGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (group.getCheckedRadioButtonId()) {
						case R.id.radioButtonTv:
							equipment = EquipmentType.TV;
							break;
						case R.id.radioButtonSom:
							equipment = EquipmentType.SOM;
							break;
						}
						if (MainActivity.D)
							Log.d(getClass().getSimpleName(),
									"Equipamento alterado: "
											+ equipment.toString());
					}
				});
	}

	private void createButtonsEvent(View view) {
		ImageButton btn_power = (ImageButton) view
				.findViewById(R.id.buttonPower);
		btn_power.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.POWER);
			}
		});

		ImageButton btn_upChannel = (ImageButton) view
				.findViewById(R.id.buttonUpChannel);
		btn_upChannel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.UP_CHANNEL);
			}
		});

		ImageButton btn_doButtonChannel = (ImageButton) view
				.findViewById(R.id.buttonDownChannel);
		btn_doButtonChannel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.DOWN_CHANNEL);
			}
		});

		ImageButton btn_upVolume = (ImageButton) view
				.findViewById(R.id.buttonUpVolume);
		btn_upVolume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.UP_VOLUME);
			}
		});

		ImageButton btn_downButton = (ImageButton) view
				.findViewById(R.id.buttonDownVolume);
		btn_downButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.DOWN_VOLUME);
			}
		});

		ImageButton btn_mute = (ImageButton) view.findViewById(R.id.buttonMute);
		btn_mute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(Command.MUTE);
			}
		});
	}

	private void sendCommand(Command command) {
		Map<String, String> params = new HashMap<String, String>();
		String url;

		if (MainActivity.D)
			Log.i(MainActivity.TAG, address + " " + command.toString() + " "
					+ nameRoom + " " + equipment);

		params.put("address", address);
		params.put("path", "sendCommand");

		params.put("command", command.toString());
		params.put("nameRoom", nameRoom);

		url = URLBuilder.createURL(params, nameRoom, equipment, command);
		new HttpSenderTask().execute("post", url);
	}

	@Override
	public void onDestroy() {
		activity.unregisterReceiver(bluetoothServiceReceive);
		if (MainActivity.D)
			Log.d(MainActivity.TAG, "Write name: " + nameRoom + " addr: "
					+ address);
		controlPreferences.setPreferences(weightStackNameRooms,
				weightStackResidenceAddress);
		super.onDestroy();
	}
}
