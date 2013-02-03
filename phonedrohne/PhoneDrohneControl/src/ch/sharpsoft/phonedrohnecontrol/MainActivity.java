package ch.sharpsoft.phonedrohnecontrol;

import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected final SparseArray<TextView> pmwTextsIn = new SparseArray<TextView>();
	protected final SparseArray<TextView> pmwTextsOut = new SparseArray<TextView>();
	protected final SparseArray<RadioButton> pmwOutRadio = new SparseArray<RadioButton>();
	protected final AtomicInteger activeChannel = new AtomicInteger(-1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		LinearLayout header = new LinearLayout(this);
		ll.addView(header);
		header.setOrientation(LinearLayout.HORIZONTAL);
		TextView channel = new TextView(this);
		channel.setText("Ch");
		header.addView(channel);
		channel.getLayoutParams().width = 30;

		TextView pmwIn = new TextView(this);
		pmwIn.setText("Pmw In");
		header.addView(pmwIn);

		TextView pmwOut = new TextView(this);
		pmwOut.setText("Pmw Out");
		header.addView(pmwOut);

		TextView radio = new TextView(this);
		radio.setText("Selection");
		header.addView(radio);

		int width = 400;// header.getWidth();
		channel.getLayoutParams().width = (int) (width * 0.1f);
		pmwIn.getLayoutParams().width = (int) (width * 0.3f);
		pmwOut.getLayoutParams().width = (int) (width * 0.3f);
		radio.getLayoutParams().width = (int) (width * 0.3f);

		for (int i = 0; i < 8; i++) {
			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);
			ll.addView(row);

			TextView ch = new TextView(this);
			ch.setText(i + "");
			row.addView(ch);
			ch.getLayoutParams().width = (int) (width * 0.2f);

			TextView tvIn = new TextView(this);
			tvIn.setText("0000");
			row.addView(tvIn);
			pmwTextsIn.put(i, tvIn);
			tvIn.getLayoutParams().width = (int) (width * 0.3f);

			TextView tvOut = new TextView(this);
			tvOut.setText("1000");
			row.addView(tvOut);
			pmwTextsOut.put(i, tvOut);
			tvOut.getLayoutParams().width = (int) (width * 0.3f);

			RadioButton radioButton = new RadioButton(this);
			row.addView(radioButton);
			pmwOutRadio.put(i, radioButton);
			radioButton.getLayoutParams().width = (int) (width * 0.2f);

			final int chi = i;

			radioButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								activeChannel.set(chi);
								for (int j = 0; j < 8; j++) {
									if (j != chi) {
										pmwOutRadio.get(j).setChecked(false);
									}
								}
							}
						}
					});
		}
		Button selectAll = new Button(this);
		selectAll.setText("Select all");
		selectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activeChannel.set(-1);
				for (int j = 0; j < 8; j++) {
					pmwOutRadio.get(j).setChecked(false);
				}
			}
		});
		ll.addView(selectAll);
		SeekBar sb = new SeekBar(this);
		sb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ll.addView(sb);
		sb.setMax(1000);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					short pmw = (short) (1000 + progress);
					byte ch = activeChannel.byteValue();
					if (ch < 0) {
						for (byte i = 0; i < 8; i++) {
							sendCommand(i, pmw);
						}
					} else {
						sendCommand(ch, pmw);
					}
				}
			}
		});
	}

	public synchronized void sendCommand(byte line, short pmw) {
		pmwTextsOut.get(Integer.valueOf(line)).setText(pmw + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}