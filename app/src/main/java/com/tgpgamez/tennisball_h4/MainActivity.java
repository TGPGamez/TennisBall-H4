package com.tgpgamez.tennisball_h4;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm;
    ImageView ball;
    List sensorList;

    //Create a new SensorEventListener
    SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Get sensorEvent values
            float[] values = sensorEvent.values;

            //Calculate new X position
            float newX = ball.getX() - values[0] * 5;
            //Calculate new Y position
            float newY = ball.getY() + values[1] * 5;

            //Check if new position is withing metrics
            if(isPositionOutOfBounds(newX, newY) == false){
                //Set X
                ball.setX(newX);
                //Set Y
                ball.setY(newY);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    /**
     * Method to check if x and y position is out of metric
     * @param newX X coordinate
     * @param newY Y coordinate
     * @return
     */
    boolean isPositionOutOfBounds(float newX, float newY){
        //Get DisplayMetrics
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //Calculate height of screen/metric
        Float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //Calculate width of screen/metric
        Float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if(newX > 0 - ball.getMeasuredWidth() / 2  && (newX + ball.getMeasuredWidth()) / 2 < dpWidth * 2 &&
            newY > 0 - ball.getMeasuredHeight() / 2  && (newY - ball.getMeasuredHeight() / 2) < dpHeight * 2)
        {
            //Rotate ball
            ball.animate().rotationBy((newX + newY) * 5).start();
            return false;
        }
        ball.animate().rotation(0);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        ball = findViewById(R.id.ball_img);
        sensorList = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(sel, (Sensor)sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(sel);

        super.onStop();
    }
}