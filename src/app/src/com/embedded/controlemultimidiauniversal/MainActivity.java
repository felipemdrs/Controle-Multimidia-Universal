package com.embedded.controlemultimidiauniversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.embedded.controlemultimidiauniversal.Interface.INamedRoom;
import com.embedded.controlemultimidiauniversal.Interface.IResidenceAddress;
import com.embedded.controlemultimidiauniversal.bluetooth.BluetoothService;
import com.embedded.controlemultimidiauniversal.bluetooth.BluetoothServiceReceiver;
import com.embedded.controlemultimidiauniversal.net.LoadRoomsTask;
import com.embedded.controlemultimidiauniversal.net.SearchResidenceTask;
import com.embedded.controlemultimidiauniversal.net.http.HttpUtils;
import com.embedded.controlemultimidiauniversal.room.OptionsCopyConfiguration;
import com.embedded.controlemultimidiauniversal.room.Room;
import com.embedded.controlemultimidiauniversal.room.adapter.RoomAdapter;
import com.embedded.controlemultimidiauniversal.room.adapter.VerifiyNameRoomTask;
import com.embedded.controlemultimidiauniversal.sharedPreferences.ControlPreferences;
import com.embedded.controlemultimidiauniversal.sharedPreferences.WeightStack;
import com.embedded.controlemultimidiauniversal.sleep.CustomNumberPick;
import com.example.aulatabview.R;

public class MainActivity extends FragmentActivity implements INamedRoom,
		IResidenceAddress {

	public static boolean D = false;
	public static boolean RUN_IN_DEVICE = true;
	public static final String TAG = "Debug_Controle";

	// ---------- Novo ----------------------------

	private BluetoothServiceReceiver mBluetoothServiceReceive;
	private WeightStack mWeightStackNameRooms = new WeightStack();
	private WeightStack mWeightStackResidenceAddress = new WeightStack();
	private String mNameRoom = "";
	private String mResidenceAddress;
	private TextView mTxt_nameRoom;
	private ControlPreferences mControlPreferences;
	private RoomAdapter mAdapterListRoom;
	private List<Room> mListRooms = new ArrayList<Room>();

	// ---------- Novo ----------------------------

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(1);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			private long mTimeUpdateRooms = 0;
			private LoadRoomsTask mLoadRoomsAsynk;
			
			private boolean loadRoomsNow() {
				return mTimeUpdateRooms == 0
						|| (System.currentTimeMillis() - mTimeUpdateRooms) >= 15000;
			}

			@Override
			public void onPageSelected(int pagePosition) {
				if (pagePosition == 2) {

					ListView list = (ListView) findViewById(R.id.listViewRooms);
					mAdapterListRoom = new RoomAdapter(mViewPager.getContext(),
							mListRooms);
					list.setAdapter(mAdapterListRoom);
					ProgressBar prog = (ProgressBar) findViewById(R.id.progressBar1);
					TextView msgLoad = (TextView) findViewById(R.id.textView1);
					if (mListRooms.size() > 0) {
						TextView logLoadRoom = (TextView) findViewById(R.id.logLoadRoom);
						logLoadRoom.setVisibility(View.GONE);
					}

					Log.d(MainActivity.TAG,
							"Tempo: "
									+ (System.currentTimeMillis() - mTimeUpdateRooms)
									+ "");
					if (loadRoomsNow()) {
						mLoadRoomsAsynk = new LoadRoomsTask(mViewPager.getContext(),
								mAdapterListRoom, mListRooms);
						prog.setVisibility(View.VISIBLE);
						msgLoad.setVisibility(View.VISIBLE);
						mLoadRoomsAsynk.execute(getResidenceAddress()
								+ "/getRoomConfigurations");
						mTimeUpdateRooms = System.currentTimeMillis();
					} else {
						mAdapterListRoom.setList(mListRooms);
						mAdapterListRoom.notifyDataSetChanged();
						prog.setVisibility(View.GONE);
						msgLoad.setVisibility(View.GONE);

					}
					startUpClickListView();
				}else{
					/*CustomNumberPick customNumberPickTV_min = (CustomNumberPick) findViewById(R.id.tv_sleep_min);
					CustomNumberPick customNumberPickTV_seg = (CustomNumberPick) findViewById(R.id.tv_sleep_seg);
					CustomCountDownTime timerCount = new CustomCountDownTime(
							(long) (1000000), 1000, customNumberPickTV_min, customNumberPickTV_seg);
					timerCount.start();*/
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Log.d(TAG, "onpageScroolled " + arg0 + arg1 + arg2);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// Log.d(TAG, "onpageScroolState " + arg0);

			}
		});

		getSharedPreference();
		createBluetoothServiceIntent();

		callSearchResidence();
		
		

	}

	private void callSearchResidence() {
		if (HttpUtils.hasConnection(getApplicationContext())
				&& MainActivity.RUN_IN_DEVICE) {
			SearchResidenceTask searchResidence = new SearchResidenceTask(this);
			searchResidence.execute();
		} else
			HttpUtils.showConnectionFail(getApplicationContext());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	private void createBluetoothServiceIntent() {
		IntentFilter filter = new IntentFilter(
				BluetoothServiceReceiver.BLUETOOTH_RESULT);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		mBluetoothServiceReceive = new BluetoothServiceReceiver(this);
		registerReceiver(mBluetoothServiceReceive, filter);

		Intent bluetoothIntent = new Intent(this, BluetoothService.class);
		startService(bluetoothIntent);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.d(TAG, "position: " + position);
			Fragment fragment = new DummySectionFragment(position);
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		int positionPage;

		public DummySectionFragment(int positionPage) {
			this.positionPage = positionPage;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;

			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 0:

				rootView = inflater.inflate(R.layout.activity_sleep, container,
						false);
				Log.d(TAG, "case 0");

				break;
			case 1:
				rootView = inflater.inflate(R.layout.control, container, false);
				Log.d(TAG, "case 1");
				break;
			case 2:
				rootView = inflater.inflate(R.layout.activity_list_rooms,
						container, false);
				Log.d(TAG, "case 2");

				break;

			default:
				break;
			}

			return rootView;
		}

	}

	public void getSharedPreference() {
		mControlPreferences = new ControlPreferences(this);
		Map<String, String> preferences = mControlPreferences.getPreferences();

		String sharedPreferenceAddress = preferences
				.get(ControlPreferences.PREFS_RESIDENCE_ADDRESS);
		String sharedPreferenceNameRooms = preferences
				.get(ControlPreferences.PREFS_NAME_ROOMS);

		if (sharedPreferenceAddress != null)
			mWeightStackResidenceAddress.create(sharedPreferenceAddress);
		if (sharedPreferenceNameRooms != null)
			mWeightStackNameRooms.create(sharedPreferenceNameRooms);
	}

	@Override
	public void setNameRoom(String nameRoom) {
		if (nameRoom != null) {
			this.mNameRoom = nameRoom;

			if (mTxt_nameRoom == null)
				mTxt_nameRoom = (TextView) findViewById(R.id.textViewNameRoom);

			mWeightStackNameRooms.add(nameRoom);
			mControlPreferences.setNamePreference(mWeightStackNameRooms);

			mTxt_nameRoom.setText(this.mNameRoom);
			Log.d("Info", "setAdd " + mWeightStackNameRooms.toString());
		}
	}

	@Override
	public String getNameRoom() {
		return mNameRoom;
	}

	@Override
	public void setResidenceAddress(String residenceAddress) {
		if (residenceAddress != null) {
			this.mResidenceAddress = residenceAddress;

			mWeightStackResidenceAddress.add(residenceAddress);

			mControlPreferences
					.setAddressPreference(mWeightStackResidenceAddress);
			VerifiyNameRoomTask verifiyNameRoomTask = new VerifiyNameRoomTask(
					this, mWeightStackNameRooms, residenceAddress);
			verifiyNameRoomTask.execute();
		}
	}

	@Override
	public String getResidenceAddress() {
		return mResidenceAddress;
	}

	public void startUpClickListView() {
		ListView listViewRooms = (ListView) findViewById(R.id.listViewRooms);
		listViewRooms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String nameRoomCopy = mListRooms.get(position).getName();
				Log.d(MainActivity.TAG, nameRoomCopy);
				OptionsCopyConfiguration optionsCopy = new OptionsCopyConfiguration(
						mViewPager.getContext(), nameRoomCopy);
				optionsCopy.setTitle("config de copia");
				optionsCopy.setCanceledOnTouchOutside(true);
				optionsCopy.show();

			}
		});
	}
	
	private class CustomCountDownTime extends CountDownTimer{

		private CustomNumberPick customNumberPickMin;
		private CustomNumberPick customNumberPickSeg;
		
    	public CustomCountDownTime(long millisInFuture, long countDownInterval, CustomNumberPick customNumberPickMin, CustomNumberPick customNumberPickSeg) {
    		super(millisInFuture, countDownInterval);
    		this.customNumberPickMin = customNumberPickMin;
    		this.customNumberPickSeg = customNumberPickSeg;
    	}

    	@Override
    	public void onTick(long millisUntilFinished) {
    		String segundos = String
    				.valueOf((int) (millisUntilFinished / 1000 % 60));
    		String minutos = String
    				.valueOf((int) millisUntilFinished / 1000 / 60);
    		if (String.valueOf(segundos).length() == 1) {
    			segundos = "0" + String.valueOf(segundos);
    		}
    		if (String.valueOf(minutos).length() == 1) {
    			minutos = "0" + String.valueOf(minutos);
    		}
    		customNumberPickSeg.setValue(Integer.valueOf(segundos));
    		customNumberPickMin.setValue(Integer.valueOf(minutos));
    		Log.i("Min", segundos); 
    	}

    	@Override
    	public void onFinish() {
    		// TODO Auto-generated method stub
    	}
    }

}
