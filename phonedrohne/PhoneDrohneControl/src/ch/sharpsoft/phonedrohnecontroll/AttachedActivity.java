package ch.sharpsoft.phonedrohnecontroll;

import org.mavlink.messages.PD_LINE;
import org.mavlink.messages.phonedrohne.msg_line_value;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class AttachedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new LinearLayout(this));
		msg_line_value msg = new msg_line_value(0, 0);
		msg.line = PD_LINE.PD_LINE_IN_0;
		msg.pmw 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
