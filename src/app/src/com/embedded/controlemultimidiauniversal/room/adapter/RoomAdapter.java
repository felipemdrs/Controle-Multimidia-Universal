package com.embedded.controlemultimidiauniversal.room.adapter;

import com.embedded.controlemultimidiauniversal.room.Room;
import com.example.aulatabview.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RoomAdapter extends BaseAdapter {

	private List<Room> listRooms;

	private LayoutInflater myLayout;

	public RoomAdapter(Context context, List<Room> rooms) {
		this.listRooms = rooms;
		this.myLayout = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return listRooms.size();
	}

	@Override
	public Object getItem(int position) {
		return listRooms.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = myLayout.inflate(R.layout.adapter_room, null);
		Collections.sort(listRooms);

		if (listRooms.size() > 0) {
			Room roomTemp = listRooms.get(position);

			TextView nameRoom = (TextView) convertView
					.findViewById(R.id.nameRoom);
			nameRoom.setText(roomTemp.getName());

			TextView tvStatus = (TextView) convertView
					.findViewById(R.id.statusTV);
			tvStatus.setText("Status: "
					+ roomTemp.getEquipmentByName("tv").getStatusPower()
							.toString());

			TextView tvChannel = (TextView) convertView
					.findViewById(R.id.channelTV);
			tvChannel.setText("Volume: "
					+ roomTemp.getEquipmentByName("tv").getVolume());

			TextView somStatus = (TextView) convertView
					.findViewById(R.id.statusSom);
			somStatus.setText("Status: "
					+ roomTemp.getEquipmentByName("sound").getStatusPower()
							.toString());

			TextView somChannel = (TextView) convertView
					.findViewById(R.id.channelSom);
			somChannel.setText("Volume: "
					+ roomTemp.getEquipmentByName("sound").getVolume());
		}

		return convertView;
	}

	public void setList(List<Room> newList) {
		listRooms = newList;
	}

}
