package dk.dreamingit.pvc;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class GeigerPlayer  {
    
    GeigerPlayerSound tickPool;
    GeigerPlayerSound tockPool;
    boolean mRunning = false;
    int mCount = 0;
    int mPeriod = 1;
    private long mTickDuration;
    //private long mStopTimeInFuture;
    Context fieldCtx;
    
    public GeigerPlayer(Context ctx) {
            
            tickPool = new GeigerPlayerSound(ctx, 10, R.raw.geiger);
            tockPool = new GeigerPlayerSound(ctx, 10, R.raw.geiger);
            fieldCtx = ctx;
            
    }
    
    public void onStart(int secDelayPerTic) {
            int period = 4;
    		mRunning = true;
            mPeriod = (period == 0) ? 1 : period;
            mCount = 0;
            
            mTickDuration = 1000 * secDelayPerTic;
            run();
            //Log.i("metronome", "tid: " + Thread.currentThread().getId() + " onStart");
            
    }
    
    private  void run() {
            //Log.i("metronome", "tid: " + Thread.currentThread().getId() + " run");
            if (tockPool == null && tickPool == null)
            		return;
            
            if (!mRunning)
                    return;
            
            //mStopTimeInFuture = SystemClock.elapsedRealtime() + mTickDuration;
            
            if ((mPeriod != 1) && (++mCount % mPeriod == 0)) {
                    mCount = 0;
            
                    tockPool.start();
            } else {
                    
                    tickPool.start();
            }
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
            
    }
    
    public void onStop() {
            mRunning = false;
            mCount = 0;
            tickPool.stop();
            tockPool.stop();
            mHandler.removeMessages(MSG);
            //mHandler.sendMessage(mHandler.obtainMessage(MSG));
            
    }
    
    public void onDestroy() {
            onStop();
    
            tickPool.onDestroy();
            tockPool.onDestroy();
            
    }


    
    private static final int MSG = 1;
    
    private Handler mHandler = new Handler() {
            public void handleMessage(Message m) {
                    run();
            }
    };

}