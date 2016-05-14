package svmclassifier.zhuke.com.action_record;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
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

    private TextView info;
    private TextView loginUser;

    private TextView timestampv;

    private Button threadTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SVMConfig.init();
        Intent intent = new Intent(this, LoginActivity.class);
        if (SVMConfig.loginUserId == 0) {
            startActivity(intent);
        } else {

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
            info = (TextView) findViewById((R.id.info));
            loginUser = (TextView) findViewById(R.id.loginUser);

            info.setText(getResources().getString(R.string.info) + "\n");
            loginUser.setText(getResources().getString(R.string.loginUser) + SVMConfig.loginUserName + "\n");

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(this, linearAcc, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            aX.setText(getResources().getString(R.string.ax) + String.valueOf(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            aY.setText(getResources().getString(R.string.ay) + String.valueOf(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            aZ.setText(getResources().getString(R.string.az) + String.valueOf(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");

            ActionRecorder.setAx(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setAy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setAz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            oX.setText(getResources().getString(R.string.ox) + String.valueOf(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            oY.setText(getResources().getString(R.string.oy) + String.valueOf(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            oZ.setText(getResources().getString(R.string.oz) + String.valueOf(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");

            ActionRecorder.setOy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setOz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gX.setText(getResources().getString(R.string.gx) + String.valueOf(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            gY.setText(getResources().getString(R.string.gy) + String.valueOf(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");
            gZ.setText(getResources().getString(R.string.gz) + String.valueOf(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "\n");

            ActionRecorder.setGx(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setGy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setGz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }
        timestampv.setText(getResources().getString(R.string.timestamp) + String.valueOf(System.currentTimeMillis()) + "\n");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            SVMConfig.isUpdateBuffer = true;
            //填充数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (SVMConfig.isUpdateBuffer) {
                        try {
                            ActionSender.updateBuffer();
                            Thread.currentThread().sleep(SVMConfig.threadTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        SVMConfig.isUpdateBuffer = false;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Thread thread = new Thread(new ActionSender());
            thread.start();
        }
        return true;
    }
}
