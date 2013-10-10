package dk.dreamingit.pvc;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NodeEnd extends EmptyNode {

	private boolean win = MainActivity.win;
	private MediaPlayer mediaPlayer;
	ServerService server;
	//Check every 5 sec
    final Handler h = new Handler();
    final int delay = 5000;//milli seconds
	
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
				post("statuseg", "USADone");
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
				post("statuseg", "USSRDone");
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
		      //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
		    }

		    public void onServiceDisconnected(ComponentName className) {
		    	//Toast.makeText(getApplicationContext(), "DisConnected", Toast.LENGTH_SHORT).show();
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
	public void post(String Nnode, String Nvalue)
	{
		final String Fnode = Nnode;
		final String Fvalue = Nvalue;
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		    	String node = Fnode;
		    	String value = Fvalue;
		        try {
		           if (server != null)
		           {
		        	   server.postConnection(node, value);
		           }
		        	
		    		    }
		    		catch (Exception e)
		    		{ 	Log.i("postNode -server:", server.toString());
		    			Log.i("postNodeException: ", e.getStackTrace()[0].toString());	
		    		e.printStackTrace();
		    		    	}
		    		    
		    }
		});

		thread.start(); 
		try {
			thread.join();
		} catch (InterruptedException e) {
			Toast.makeText(getApplicationContext(), "INTERRUPTED, BIATCH", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onPause(){
		super.onPause();
		h.removeCallbacks(repeater);
		unbindService(mConnection);
	}
	
	protected void onResume(){
		super.onResume();
		h.postDelayed(repeater,delay);
	}
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ServerService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private void postWin()
	{
		if (team.equals("USA"))
		{
			if(win)
			{
				post("statuseg", "USADone");
			} else
			{
				setStory(R.string.usa_endlose);
			}
		} else
		{
			if(win)
			{
				post("statuseg", "USSRDone");
			} else
			{
				setStory(R.string.ussr_endlose);
			}
		}
	}
	
	private Runnable repeater = new Runnable(){
        public void run(){
            postWin();
            
            if (win){
            	h.postDelayed(this,delay);
            	try {
    				server.updateAllFlags();
    			} catch (IsTerribleException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            } else
            {h.removeCallbacks(repeater);}
            
        }      
	};
}
