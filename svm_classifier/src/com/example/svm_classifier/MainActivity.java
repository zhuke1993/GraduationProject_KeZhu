package com.example.svm_classifier;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager = null;
	private Sensor orientationSensor = null;
	private Sensor linearAcc = null;
	private Sensor gyroscopeSensor = null;
	private TextView oX;
	private TextView oY;
	private TextView oZ;

	private TextView aX;
	private TextView aY;
	private TextView aZ;
	
	private TextView gX;
	private TextView gY;
	private TextView gZ;

	private TextView timestampv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		oX = (TextView) findViewById(R.id.ox);
		oY = (TextView) findViewById(R.id.oy);
		oZ = (TextView) findViewById(R.id.oz);

		aX = (TextView) findViewById(R.id.ax);
		aY = (TextView) findViewById(R.id.ay);
		aZ = (TextView) findViewById(R.id.az);

		gX = (TextView) findViewById(R.id.gx);
		gY = (TextView) findViewById(R.id.gy);
		gZ = (TextView) findViewById(R.id.gz);
		
		timestampv = (TextView) findViewById(R.id.timestampv);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		orientationSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		linearAcc = sensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			aX.setText("ACCELERATION X: " + event.values[0]);
			aY.setText("ACCELERATION Y: " + event.values[1]);
			aZ.setText("ACCELERATION Z: " + event.values[2]);
		} else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			oX.setText("ORIENTATION X: " + event.values[0]);
			oY.setText("ORIENTATION Y: " + event.values[1]);
			oZ.setText("ORIENTATION Z: " + event.values[2]);
		} else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
			gX.setText("GYROSCOPE X: " + event.values[0]);
			gY.setText("GYROSCOPE Y: " + event.values[1]);
			gZ.setText("GYROSCOPE Z: " + event.values[2]);
		}
		timestampv.setText("Timestamp " + System.currentTimeMillis());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(this); // ½â³ý¼àÌýÆ÷×¢²á
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(this, orientationSensor,
				SensorManager.SENSOR_DELAY_NORMAL); // Îª´«¸ÐÆ÷×¢²á¼àÌýÆ÷
		sensorManager.registerListener(this, linearAcc,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, gyroscopeSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}
