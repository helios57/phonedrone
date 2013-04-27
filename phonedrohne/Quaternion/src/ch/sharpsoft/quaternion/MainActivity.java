package ch.sharpsoft.quaternion;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;
	private Handler handler;
	private TextView tw;
	private float[] accel = new float[3];
	private float[] magnet = new float[3];
	private float integrationFactor = 0.1f;

	public MainActivity() {

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler(Looper.getMainLooper());
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		final LinearLayout ll = new LinearLayout(this);
		tw = new TextView(this);

		ll.addView(tw);
		setContentView(ll);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return true;
	}

	@Override
	public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

	}

	static int counter = 0;

	@Override
	public void onSensorChanged(final SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			for (int i = 0; i < 3; i++) {
				accel[i] = accel[1] * (1 - integrationFactor) + event.values[i]
						* integrationFactor;
			}
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			for (int i = 0; i < 3; i++) {
				magnet[i] = magnet[1] * (1 - integrationFactor)
						+ event.values[i] * integrationFactor;
			}
		}
		if (counter++ % 10 == 0) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					StringBuilder sb = new StringBuilder();
					sb.append(counter++);
					sb.append("\n");
					sb.append("accel ");
					for (float f : accel) {
						sb.append(String.format("%.4f", f)).append(" ");
					}
					sb.append("\n");
					sb.append("magnet ");
					for (float f : magnet) {
						sb.append(String.format("%.4f", f)).append(" ");
					}
					sb.append("\n");
					float[] R = new float[16];
					float[] I = new float[16];
					SensorManager.getRotationMatrix(R, I, accel, magnet);

					sb.append("R\n");
					for (int i = 0; i < 4; i++) {
						sb.append("[");
						for (int j = 0; j < 4; j++) {
							sb.append(String.format("%.4f", R[i * 4 + j]))
									.append(" ");
						}
						sb.append("]\n");
					}
					sb.append("I\n");
					for (int i = 0; i < 4; i++) {
						sb.append("[");
						for (int j = 0; j < 4; j++) {
							sb.append(String.format("%.4f", R[i * 4 + j]))
									.append(" ");
						}
						sb.append("]\n");
					}
					float[] quaternion = new float[4];
					SensorManager.getOrientation(R, quaternion);

					sb.append("quaternion ");
					for (float f : quaternion) {
						sb.append(String.format("%.4f", f)).append(" ");
					}
					sb.append("\n");

					float inclination = SensorManager.getInclination(I);

					sb.append("inclination ");
					sb.append(String.format("%.4f", inclination));
					sb.append("\n");

					tw.setText(sb.toString());
				}
			});
		}
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, mMagnetometer,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}
