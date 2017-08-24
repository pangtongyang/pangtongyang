package lbcy.com.cn.wristband.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import lbcy.com.cn.wristband.R;


public class OprateActivity extends Activity implements View.OnClickListener,OnCheckedChangeListener{
	private Button button1, button2, button3, button4, button5, button6, button36, button11, button12;

	private Button alarm1, alarm2;
	private TextView config;
	private Button button24, button25, button26;// zh , en , steplen
	private CheckBox vibrate, press, check11;

	private static final String TAG = OprateActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_oprate);

		initViews();
		super.onCreate(savedInstanceState);
	}



	private void initViews() {
		vibrate = (CheckBox) findViewById(R.id.check12);
		press = (CheckBox) findViewById(R.id.press);
		check11 = (CheckBox) findViewById(R.id.check11);
		button24 = (Button) findViewById(R.id.button24);
		button36 = (Button) findViewById(R.id.button36);
		button25 = (Button) findViewById(R.id.button25);
		button1 = (Button) findViewById(R.id.button1);
		button11 = (Button) findViewById(R.id.button11);
		button12 = (Button) findViewById(R.id.button12);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button6 = (Button) findViewById(R.id.button6);
		button26 = (Button) findViewById(R.id.button26);
		alarm1 = (Button) findViewById(R.id.alarm1);
		alarm2 = (Button) findViewById(R.id.alarm2);
		config = (TextView) findViewById(R.id.config);
		button1.setOnClickListener(this);
		button36.setOnClickListener(this);
		button11.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button24.setOnClickListener(this);
		button25.setOnClickListener(this);
		button6.setOnClickListener(this);
		button26.setOnClickListener(this);
		alarm1.setOnClickListener(this);
		button12.setOnClickListener(this);
		alarm2.setOnClickListener(this);
		vibrate.setOnCheckedChangeListener(this);
		check11.setOnCheckedChangeListener(this);
		press.setOnCheckedChangeListener(this);
	}


	@Override
	public void onClick(View view) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.check11:
				break;
			case R.id.check12:
				//bleService.SendVibrateSet(isChecked);
				break;
			case R.id.press:

				break;
		}
	}
}
