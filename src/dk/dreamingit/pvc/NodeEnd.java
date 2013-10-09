package dk.dreamingit.pvc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class NodeEnd extends EmptyNode {

	private boolean win = true;
	private MediaPlayer mediaPlayer;
	ServerService server;
	
	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inflateNarrative() {
		

		if (team.equals("USA"))
		{
			if(win)
			{
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.usaanthem);
				setStory(R.string.usa_endwin);	
				server.postConnection("statuseg", "USADone");
			} else
			{
				setStory(R.string.usa_endlose);
			}
		} else
		{
			if(win)
			{
				setStory(R.string.ussr_endwin);	
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ussranthem);
				server.postConnection("statuseg", "USSRDone");
			} else
			{
				setStory(R.string.ussr_endlose);
			}
		}
		
		if(!win)
		{
			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sadsongforbrokenhearts);
		}
		mediaPlayer.start();
		
	}

	
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
protected void onStart() {
  super.onStart();
  
// Bind to LocalService
  //h.postDelayed(repeater, delay);
  Intent intent = new Intent(this, ServerService.class);
  bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
  
}
	
	@Override
	protected void onPause(){
		super.onPause();
		unbindService(mConnection);
	}
}
