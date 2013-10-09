package dk.dreamingit.pvc;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.os.IBinder;
import android.content.ComponentName;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SelectTeamActivity extends Activity {
	// Identifiers
	public final static String EXTRA_MESSAGE = "dk.dreamingit.pvc.TEAM";
	public final static String CURRENT_NODE = "dk.dreamingit.pvc.NODE";
	public final static String VOICE_SCORE = "dk.dreamingit.pvc.VOICE_SCORE";
	protected PowerManager.WakeLock mWakeLock;
	private ServerService server;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_team);
		
		/* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        
	}
	
	@Override
    protected void onStart() {
        super.onStart();
     // Bind to LocalService
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        

    } private ServiceConnection mConnection = new ServiceConnection() {

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
	protected void onPause(){
		super.onPause();
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
		getMenuInflater().inflate(R.menu.select_team, menu);
		return true;
	}

	public void startUSSR(View v)
	{
 		Intent intent = new Intent(this, SpeechNode.class);
		intent.putExtra(EXTRA_MESSAGE, "USSR");
		server.postConnection("gamestart", "USSR");
		startActivity(intent);
	}
	
	public void startUSA(View v)
	{
		Intent intent = new Intent(this, SpeechNode.class);
		intent.putExtra(EXTRA_MESSAGE, "USA");
		server.postConnection("gamestart", "USA");
		
		startActivity(intent);
	}
}
