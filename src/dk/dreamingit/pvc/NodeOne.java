package dk.dreamingit.pvc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

public class NodeOne extends EmptyNode
{
	private boolean arrivedFirst = MainActivity.NodeOneWon;
	private ServerService server;

	
	
	@Override
	public void goNext(View V) {
			Intent intent = createNextIntent(MainActivity.class);
			startActivity(intent);		
	}
	
	@Override
    protected void onStart() {
        super.onStart();
		//Setup player
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.vednypost);
		mediaPlayer.start();
		//Vibrator
		Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		v.vibrate(1000);
		
     // Bind to LocalService
        //h.postDelayed(repeater, delay);
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
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
			protected void onPause(){
				super.onPause();
				unbindService(mConnection);
		  }

	@Override
	protected void inflateNarrative() {
		if (team.equals("USA"))
		{
			if (arrivedFirst)
			{
				setStory(R.string.usa_1_direct);
				server.postConnection("statusnode1", "no");
				
			} else
			{
				setStory(R.string.usa_1_detour);
			}
		} else
		{
			if (arrivedFirst)
			{
				setStory(R.string.ussr_1_direct);
				server.postConnection("statusnode1", "no");
			} else
			{
				setStory(R.string.ussr_1_detour);
			}
		}
		
	}

}
