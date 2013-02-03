package ch.sharpsoft.phonedrohnecontrol;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

@SuppressLint("UseSparseArrays")
public class AttachedActivity extends MainActivity implements Runnable {
	private static final String TAG = "PhoneDrohne";

	private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";

	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	private UsbManager mUsbManager;
	private UsbAccessory mAccessory;
	private ParcelFileDescriptor mFileDescriptor;
	private FileInputStream mInputStream;
	private FileOutputStream mOutputStream;

	private Handler mHandler = new Handler();

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "onReceive " + intent);
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	private void openAccessory(UsbAccessory accessory) {
		Log.d(TAG, "openAccessory");
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "PhoneDrohneReciever");
			thread.start();
			Log.d(TAG, "accessory opened");
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory() {
		Log.d(TAG, "closeAccessory");
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	public void run() {
		Log.d(TAG, "run");
		int ret = 0;
		byte[] buffer = new byte[4];

		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
				Log.d(TAG, "reader.getNextMessage() (after) ret=" + ret);
				if (ret > 0) {
					Log.d(TAG, "reader.getNextMessage() != null");
					byte action = buffer[0];
					switch (action) {
					case 0: { // ch in
						final byte ch = buffer[1];
						final int pmw = (((short) (buffer[2] & 0xFF)) << 8)
								+ ((short) (buffer[3] & 0xFF));
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								if (pmwTextsIn.get(Integer.valueOf(ch)) != null) {
									pmwTextsIn.get(Integer.valueOf(ch))
											.setText("" + pmw);
								}
							}
						});
						break;
					}
					default:
						break;

					}
				}
			} catch (IOException e) {
				Log.d(TAG, "run error", e);
				return;
			}
			if (ret > 0) {
				buffer = new byte[512];
				ret = 0;
			}
		}
		Log.d(TAG, "run after reader");
	}

	@Override
	public synchronized void sendCommand(byte line, short pmw) {
		super.sendCommand(line, pmw);
		Log.d(TAG, "sendCommand");
		try {
			byte[] result = new byte[4];
			result[0] = 1;
			result[1] = line;
			result[2] = (byte) (pmw >>> 8);
			result[3] = (byte) (pmw);
			mOutputStream.write(result);
			mOutputStream.flush();
			mOutputStream.getFD().sync();
		} catch (IOException e) {
			Log.d(TAG, "sendCommand fail", e);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		mUsbManager = UsbManager.getInstance(this);

		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}
		Log.d(TAG, "onCreate end");
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		getIntent();
		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}