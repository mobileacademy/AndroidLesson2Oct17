package ro.mobileacademy.newsreaderapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by valerica.plesu on 21/10/2017.
 */

public class CounterAsyncTask extends AsyncTask<Integer, Void, Integer> {

    private static final String TAG = CounterAsyncTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // is executed in the main thread
        Log.d(TAG, "onPreExecute");
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        Log.d(TAG, "doInBackground - background thread");

        int endValue = integers[0];

        return testCount(endValue);
    }

    @Override
    protected void onPostExecute(Integer sum) {
        super.onPostExecute(sum);
        Log.d(TAG, "task is done! - on main  thread SUM=" + sum);

//        Toast.makeText(NewsReaderApplication.getInstance(), "sum= "+ sum, Toast.LENGTH_SHORT).show();
    }

    private int testCount(int endValue) {
        int i = 0;
        int sum = 0;
        while (i < endValue) {
            i++;

            sum+=i;

            Log.d(TAG, "second: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exp) {
                // Auto-generated catch block
                exp.printStackTrace();
            }
        }
        return sum;
    }
}
