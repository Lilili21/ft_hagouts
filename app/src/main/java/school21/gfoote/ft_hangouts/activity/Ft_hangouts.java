package school21.gfoote.ft_hangouts.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public final class Ft_hangouts extends Application implements Application.ActivityLifecycleCallbacks
{
    private static final int APP_VISIBLE = 0;
    private static final int APP_HIDDEN = 1;
    private static final int VISIBILITY_DELAY_IN_MS = 300;
    private Handler visibilityHandler;


    private static final class VisibilityCallback implements Handler.Callback
    {
        private Context context;
        private int previousVisibility;
        Instant start;


        public VisibilityCallback(Context context)
        {
            this.context = context;
            previousVisibility = APP_VISIBLE;
            start = Instant.now();
        }

        @Override
        public boolean handleMessage(Message msg)
        {
            if (previousVisibility != msg.what)
            {
                previousVisibility = msg.what;
                if (msg.what == APP_VISIBLE)
                {
                    Instant end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    Toast.makeText(context, timeElapsed.getSeconds() + " seconds app was set in the background", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    start = Instant.now();
                }
            }
            return true;
        }

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        visibilityHandler = new Handler(new VisibilityCallback(getApplicationContext()));
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {

    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        visibilityHandler.removeMessages(APP_HIDDEN);
        visibilityHandler.sendEmptyMessageDelayed(APP_VISIBLE, VISIBILITY_DELAY_IN_MS);
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        visibilityHandler.removeMessages(APP_VISIBLE);
        visibilityHandler.sendEmptyMessageDelayed(APP_HIDDEN, VISIBILITY_DELAY_IN_MS);
    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {

    }

}