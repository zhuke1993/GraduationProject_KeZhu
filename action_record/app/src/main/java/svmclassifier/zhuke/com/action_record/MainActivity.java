package svmclassifier.zhuke.com.action_record;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, linearAcc, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);

        //启动数据发送线程
        new Thread(new ActionSender()).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            aX.setText("ACCELERATION X: " + new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            aY.setText("ACCELERATION Y: " + new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            aZ.setText("ACCELERATION Z: " + new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

            ActionRecorder.setAx(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setAy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setAz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            oX.setText("ORIENTATION X: " + new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            oY.setText("ORIENTATION Y: " + new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            oZ.setText("ORIENTATION Z: " + new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

            ActionRecorder.setOy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setOz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gX.setText("GYROSCOPE X: " + new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            gY.setText("GYROSCOPE Y: " + new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            gZ.setText("GYROSCOPE Z: " + new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

            ActionRecorder.setGx(new BigDecimal(event.values[0]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setGy(new BigDecimal(event.values[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            ActionRecorder.setGz(new BigDecimal(event.values[2]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }
        timestampv.setText("Timestamp " + System.currentTimeMillis());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
