package com.example.siddhantverma.accelgyro;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;

import static android.hardware.Sensor.TYPE_GYROSCOPE;

public class MainActivity extends Activity implements SensorEventListener {
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;

    private SensorManager mSensorManager;

    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private File f;
    TextView xCoor;
    TextView yCoor;
    TextView zCoor;
    TextView gyroX;
    TextView gyroY;
    TextView gyroZ;
    Button startB;
    Button stopB;
    Button save;


    ArrayList<String> Accelerometer=new ArrayList<>();
    ArrayList<String> Gyroscope=new ArrayList<>();

    // Button startB=(Button)findViewById(R.id.start);


    private final float NOISE = (float) 2.0;

    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
       // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        setContentView(R.layout.activity_main);
        funcStop();

        xCoor = (TextView) findViewById(R.id.currentX); // create X axis object
        yCoor = (TextView) findViewById(R.id.currentY); // create Y axis object
        zCoor = (TextView) findViewById(R.id.currentZ); // create Z axis object

        gyroX = (TextView) findViewById(R.id.gyroX);
        gyroY = (TextView) findViewById(R.id.gyroY);
        gyroZ = (TextView) findViewById(R.id.gyroZ);
        gyroX.setText("0.0");
        gyroY.setText("0.0");
        gyroZ.setText("0.0");
        xCoor.setText("0.0");
        yCoor.setText("0.0");
        zCoor.setText("0.0");


        startB = (Button) findViewById(R.id.start);
        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcStart();
            }
        });

        stopB = (Button) findViewById(R.id.stop);
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xCoor.setText("0.0");
                yCoor.setText("0.0");
                zCoor.setText("0.0");
                gyroX.setText("0.0");
                gyroY.setText("0.0");
                gyroZ.setText("0.0");
                funcStop();
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Data Saved ",Toast.LENGTH_SHORT).show();

                File memoryData = Environment.getExternalStorageDirectory();
                File dir = new File(memoryData.getAbsolutePath());
                dir.mkdir();
                File file = new File(dir, "DataRepresentationAccel.csv");
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    for (String s : Accelerometer)
                        os.write(s.getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File memoryData2 = Environment.getExternalStorageDirectory();
                File dir2 = new File(memoryData2.getAbsolutePath());
                dir.mkdir();
                File file2 = new File(dir2, "DataRepresentationGyro.csv");
                FileOutputStream os2 = null;
                try {
                    os2 = new FileOutputStream(file2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    for (String s : Gyroscope)
                        os2.write(s.getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
//        // add listener. The listener will be HelloAndroid (this) class
//        mSensorManager.registerListener(this,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void funcStart() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void funcStop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// can be safely ignored for this demo
    }


    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // assign directions
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            Accelerometer.add(x + "," + y + "," + z + "\n");
            xCoor.setText("X: " + x);
            yCoor.setText("Y: " + y);
            zCoor.setText("Z: " + z);
//            try {
//                fileWriter.write("ACCELEROMETER: "+x+","+y+","+"z"+"\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // assign directions
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            Gyroscope.add(x + "," + y + "," +z + "\n");
            gyroX.setText("X: " + x);
            gyroY.setText("Y: " + y);
            gyroZ.setText("Z: " + z);
//            try {
//                fileWriter.write("GYROSCOPE: "+x+","+y+","+"z"+"\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }


}