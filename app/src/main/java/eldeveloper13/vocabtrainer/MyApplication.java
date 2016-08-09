package eldeveloper13.vocabtrainer;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by ericl on 7/31/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
