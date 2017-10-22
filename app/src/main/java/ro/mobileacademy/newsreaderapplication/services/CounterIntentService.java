package ro.mobileacademy.newsreaderapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ro.mobileacademy.newsreaderapplication.activity.MainActivity;
import ro.mobileacademy.newsreaderapplication.events.CountFinishEvent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CounterIntentService extends IntentService {

    private static final String TAG = CounterIntentService.class.getSimpleName();

    public CounterIntentService() {
        super("CounterIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Log.d(TAG, "onHandleIntent");
            final String action = intent.getAction();
            if (MainActivity.COUNTER_ACTION.equals(action)) {
                testCount();

                // step 5 -> send info to application
                Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);



                //post event to eventBus
                EventBus.getDefault().post(new CountFinishEvent(10));

            } else if(MainActivity.LIST_PACKAGES.equals(action)) {
                listPackages();
            }
        }
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

    private void listPackages () {
        PackageManager pckManager = getPackageManager();

        List<ApplicationInfo> listApp
                = pckManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : listApp) {
            Log.i(TAG, "AppName= " + app.packageName);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
