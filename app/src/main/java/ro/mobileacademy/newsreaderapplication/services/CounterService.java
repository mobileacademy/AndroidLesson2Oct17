package ro.mobileacademy.newsreaderapplication.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ro.mobileacademy.newsreaderapplication.activity.MainActivity;

public class CounterService extends Service {

    private static final String TAG = CounterService.class.getSimpleName();

    public CounterService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();

        if(action == null) {
            return START_NOT_STICKY;
        }

        if (action.equalsIgnoreCase(MainActivity.COUNTER_ACTION)) {
            Log.d(TAG, "go and execute task");
            testCount();
        } else if(action.equalsIgnoreCase("LIST_PACK")) {
            //run here
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void testCount() {
        int i = 0;
        while (i < 10) {
            i++;
            Log.d(TAG, "second: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exp) {
                // Auto-generated catch block
                exp.printStackTrace();
            }
        }
    }
}
