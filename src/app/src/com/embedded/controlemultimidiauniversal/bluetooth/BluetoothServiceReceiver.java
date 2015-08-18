
package com.embedded.controlemultimidiauniversal.bluetooth;

import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothServiceReceiver extends BroadcastReceiver {

    public static final String BLUETOOTH_RESULT = "com.embedded.controlemultimidiauniversal.bluetooth.BLUETOOTH_RESULT";

    private INamedRoom setableNamedRoom;

    public BluetoothServiceReceiver(INamedRoom setableNamedRoom) {
        this.setableNamedRoom = setableNamedRoom;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String responseString = intent.getStringExtra(BluetoothService.RESPONSE_NAME_ROOM);

        setableNamedRoom.setNameRoom(responseString);
    }
}
