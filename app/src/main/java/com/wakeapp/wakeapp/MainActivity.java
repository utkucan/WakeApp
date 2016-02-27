package com.wakeapp.wakeapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class MainActivity extends Activity {

        TimePicker myTimePicker;
        Button buttonstartSetDialog;
        TextView textAlarmPrompt;
        TimePickerDialog timePickerDialog;

    int numMessages = 0;
        final static int RQS_1 = 1;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            textAlarmPrompt = (TextView) findViewById(R.id.alarmprompt);

            buttonstartSetDialog = (Button) findViewById(R.id.startAlarm);
            buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    textAlarmPrompt.setText("");
                    openTimePickerDialog(false);

                }
            });

        }

        private void openTimePickerDialog(boolean is24r) {
            Calendar calendar = Calendar.getInstance();

            timePickerDialog = new TimePickerDialog(MainActivity.this,
                    onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), is24r);
            timePickerDialog.setTitle("Set Alarm Time");

            timePickerDialog.show();

        }

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                if (calSet.compareTo(calNow) <= 0) {
                    // Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }

                setAlarm(calSet);
            }
        };

        private void setAlarm(Calendar targetCal) {

            textAlarmPrompt.setText("\n\n***\n" + "Alarm is set "
                    + targetCal.getTime() + "\n" + "***\n");

            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(), RQS_1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                    pendingIntent);

        }
    public void showNot(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
    }

    }