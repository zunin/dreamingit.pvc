package dk.dreamingit.pvc;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.widget.Toast;

public class WaitingForTeamActivity extends Activity {

	private ServerService server;
	public static String gameStarted = "";
	protected PowerManager.WakeLock mWakeLock;
    //Check every 1 sec
    final Handler h = new Handler();
    final int delay = 1000;//milli seconds
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_for_team);
		
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");

	}
	@Override
	protected void onResume() {
		super.onResume();
		h.postDelayed(repeater, delay);
	}

	@Override
    protected void onStart() {
        super.onStart();
     // Bind to LocalService
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        

    }
	
	private Runnable repeater = new Runnable(){
        public void run(){
            server.getOtherTeam();
            
            if (gameStarted.equals("USA"))
            {
            	Intent intent = new Intent(getApplicationContext(), SpeechNode.class);
        		intent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, "USA");
        		startActivity(intent);
            } else if (gameStarted.equals("USSR"))
            {
            	Intent intent = new Intent(getApplicationContext(), SpeechNode.class);
        		intent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, "USSR");
        		startActivity(intent);
            } else
            {
            	h.postDelayed(this,delay);
            }
            
            
        }
	};
	
	 private ServiceConnection mConnection = new ServiceConnection() {

		    public void onServiceConnected(ComponentName className, IBinder binder) {
		      server = ((ServerService.LocalBinder) binder).getService();
		      Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
		    }

		    public void onServiceDisconnected(ComponentName className) {
		    	Toast.makeText(getApplicationContext(), "DisConnected", Toast.LENGTH_SHORT).show();
		      //server = null;
		    }
		  };
	
	@Override
	protected void onPause(){
		super.onPause();
		h.removeCallbacks(repeater);
		unbindService(mConnection);
	}
	
	@Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waiting_for_team, menu);
		return true;
	}

}
