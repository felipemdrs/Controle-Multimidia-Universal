package com.embedded.controlemultimidiauniversal.bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService extends IntentService {

	private final String mSERVICE_NAME = "Control_Service";

	private final UUID mSEVICE_UUID = UUID
			.fromString("5b731005-45cc-4225-bb8e-7df99c1e25cc");

	private BluetoothServerSocket serverSocket = null;

	private BluetoothSocket socket = null;

	public static final String RESPONSE_NAME_ROOM = "nameRoom";

	public BluetoothService() {
		super("BluetoothService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		/*BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter != null) {
			try {
				serverSocket = bluetoothAdapter
						.listenUsingInsecureRfcommWithServiceRecord(
								mSERVICE_NAME, mSEVICE_UUID);
			} catch (IOException e) {
				Log.d("Deb", e.getMessage());
			}
			while (true) {
				try {
					Log.d("Info", "afff");
					socket = serverSocket.accept();
					if (socket != null) {
						InputStream inputStream = socket.getInputStream();
						Intent broadcastIntent = new Intent();
						broadcastIntent
								.setAction(BluetoothServiceReceiver.BLUETOOTH_RESULT);
						broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
						broadcastIntent.putExtra(RESPONSE_NAME_ROOM,
								getStringFromInputStrem(inputStream));
						sendBroadcast(broadcastIntent);
					}
				} catch (Exception e) {
				}
			}
		}*/
	}

	private static String getStringFromInputStrem(InputStream inputStream) {
		byte[] buffer = new byte[1024];
		int bytes;
		String readMessage = "";
		try {
			bytes = inputStream.read(buffer);
			readMessage = new String(buffer, 0, bytes);
		} catch (IOException e) {
		}
		return readMessage;
	}

}
