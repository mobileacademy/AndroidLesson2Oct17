package ro.mobileacademy.newsreaderapplication.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ro.mobileacademy.newsreaderapplication.utils.NewsReaderAppPref;

public class PackageInstallReceiver extends BroadcastReceiver {

    private static final String TAG = PackageInstallReceiver.class.getSimpleName();
    public static final String PACKAGE_KEY = "package";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        if (intent == null)
            return;

        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        Log.d(TAG, "packageName= " + packageName);

        NewsReaderAppPref sharedPref = new NewsReaderAppPref(context);
        sharedPref.addStringToSharePref(PACKAGE_KEY, packageName);

    }
}
