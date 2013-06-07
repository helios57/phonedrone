package ch.sharpsoft.quaternion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import ch.sharpsoft.quaternion.v1.MainActivity;
import ch.sharpsoft.quaternion.v2.SecondActivity;

public class InitialActivity extends Activity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final LinearLayout ll = new LinearLayout(this);
		final Button startV1 = new Button(this);
		startV1.setText("Start Version 1");
		startV1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(new Intent(InitialActivity.this, MainActivity.class));
			}
		});
		final Button startV2 = new Button(this);
		startV2.setText("Start Version 2");
		startV2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(new Intent(InitialActivity.this, SecondActivity.class));
			}
		});
		ll.addView(startV1);
		ll.addView(startV2);
		setContentView(ll);
	}
}
