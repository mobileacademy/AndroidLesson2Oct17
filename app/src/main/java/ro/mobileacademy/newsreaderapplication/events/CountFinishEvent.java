package ro.mobileacademy.newsreaderapplication.events;

/**
 * Created by valerica.plesu on 21/10/2017.
 */

public class CountFinishEvent {


    private int count;

    public CountFinishEvent(int countValue) {
        count = countValue;
    }

    public int getCount() {
        return count;
    }
}
