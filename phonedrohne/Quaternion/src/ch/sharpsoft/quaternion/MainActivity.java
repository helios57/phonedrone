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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mRotation;
	private Handler handler;
	private TextView tw;
	private float[] rotation = new float[3];
	private Quaternion leveled = null;

	public MainActivity() {

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler(Looper.getMainLooper());
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mRotation = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		Button level = new Button(this);
		level.setText("Level");
		level.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float[] quaterion = new float[4];
				SensorManager.getQuaternionFromVector(quaterion, rotation);
				leveled = new Quaternion(quaterion);
			}
		});
		ll.addView(level);
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
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			for (int i = 0; i < 3; i++) {
				rotation[i] = event.values[i];
			}
		}
		if (counter++ % 10 == 0) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					StringBuilder sb = new StringBuilder();
					sb.append(counter++);
					sb.append("\n");
					float[] quaterion = new float[4];
					SensorManager.getQuaternionFromVector(quaterion, rotation);
					Quaternion current = new Quaternion(quaterion);
					if (leveled == null){
						leveled = current;
					}
					sb.append("current: ");
					sb.append(current);
					sb.append("\n");
					
					sb.append("leveled: ");
					sb.append(leveled);
					sb.append("\n");
					
					Quaternion diff = current.inverse().multiply(leveled);
					
					sb.append("diff: ");
					sb.append(diff);
					sb.append("\n");
					
					tw.setText(sb.toString());
				}
			});
		}
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mRotation,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}
