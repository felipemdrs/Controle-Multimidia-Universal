package com.embedded.controlemultimidiauniversal.room;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;
import com.embedded.controlemultimidiauniversal.Interface.IResidenceAddress;
import com.embedded.controlemultimidiauniversal.net.LoadRoomsTask;
import com.example.aulatabview.R;

public class ListRooms extends Dialog {
	private Context myContext;

	private LoadRoomsTask loadRoomsAsynk;

	private INamedRoom iNamedRoom;

	private IResidenceAddress iResidenceAddress;

	public ListRooms(Context context, INamedRoom iNamedRoom) {
		super(context);
		this.myContext = context;
		this.iNamedRoom = iNamedRoom;
		this.iResidenceAddress = (IResidenceAddress) iNamedRoom;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_list_rooms);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.tittle_options_copy_configurations);

		// loadRoomsAsynk = new LoadRooms(myContext);
		// ProgressBar prog = new ProgressBar(this.getContext());
		// prog.setVisibility(View.VISIBLE);
		loadRoomsAsynk.execute(iResidenceAddress.getResidenceAddress()
				+ "/getRoomConfigurations");
		startUpClickListView();
	}

	public void startUpClickListView() {
		ListView listViewRooms = (ListView) findViewById(R.id.listViewRooms);
		listViewRooms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * //String nameRoomCopy =
				 * loadRoomsAsynk.getListRooms().get(position).getName();
				 * Log.d(MainActivity.TAG, nameRoomCopy);
				 * OptionsCopyConfiguration optionsCopy = new
				 * OptionsCopyConfiguration(myContext, iNamedRoom,
				 * nameRoomCopy); optionsCopy.setTitle("config de copia");
				 * optionsCopy.setCanceledOnTouchOutside(true);
				 * optionsCopy.show();
				 */
			}
		});

	}

}
