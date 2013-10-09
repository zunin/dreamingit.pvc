package dk.dreamingit.pvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartGameActivity extends Activity {

	protected PowerManager.WakeLock mWakeLock;
	private ServerService server;
	public static String gameStarted = "no";
	private TextView storyView;
	
	// Repeat task
	
    //Check every 5 sec
    final int delay = 5000;//milli seconds
	private final Handler h = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		
		/* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        storyView = (TextView) findViewById(R.id.start_game_description);
	}
	
	 private ServiceConnection mConnection = new ServiceConnection() {

		    public void onServiceConnected(ComponentName className, IBinder binder) {
		      server = ((ServerService.LocalBinder) binder).getService();
		      //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
		    }

		    public void onServiceDisconnected(ComponentName className) {
		    	//Toast.makeText(getApplicationContext(), "DisConnected", Toast.LENGTH_SHORT).show();
		      //server = null;
		    }
		  };
	
	@Override
	protected void onResume() {
		super.onResume();
		h.postDelayed(repeater, delay);
	}  
		  
	@Override
    protected void onStart() {
        super.onStart();
        
     // Bind to LocalService
        //h.postDelayed(repeater, delay);
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
    }
	
	private Runnable repeater = new Runnable(){
        public void run(){
            server.getGameStart();
            
            if (gameStarted.equals("yes") ||
            		gameStarted.equals("USA") || 
            		gameStarted.equals("USSR"))
            {
        		Intent intent = new Intent(getApplicationContext(), WaitingForTeamActivity.class);
        		startActivity(intent);
            } else if (gameStarted.equals("no"))
            {
            	h.removeCallbacks(repeater);
            	boolean resp = (h.postDelayed(repeater, delay));
            	//Toast.makeText(getApplicationContext(), "postDelayed: "+resp+gameStarted, Toast.LENGTH_SHORT).show();
            	//Toast.makeText(getApplicationContext(), server.getErrorcode(), Toast.LENGTH_SHORT).show();
            }
            
            
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
		getMenuInflater().inflate(R.menu.start_game, menu);
		return true;
	}
	
	public void startGameClicked(View v)
	{
		server.postConnection("gamestart", "yes");
		Intent intent = new Intent(this, SelectTeamActivity.class);
		startActivity(intent);
	}

}
