package dk.dreamingit.pvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NodeOne extends EmptyNode
{
	private boolean arrivedFirst = MainActivity.NodeOneWon;
	private ServerService server;
	//Check every 5 sec
    final Handler h = new Handler();
    final int delay = 5000;//milli seconds

    	
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
	     // Bind to LocalService
        //h.postDelayed(repeater, delay);
        Intent intent = new Intent(this, ServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	@Override
	protected void inflateNarrative() {
		if (team.equals("USA"))
		{
			if (arrivedFirst)
			{
				setStory(R.string.usa_1_direct);
				post();
				MainActivity.NodeOneFirst = true;
				
			} else
			{
				setStory(R.string.usa_1_detour);
			}
		} else
		{
			if (arrivedFirst)
			{
				setStory(R.string.ussr_1_direct);
				post();
				MainActivity.NodeOneFirst = true;
				
			} else
			{
				setStory(R.string.ussr_1_detour);
			}
		}
		
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

	
	        	public void post()
	        	{
	        		Thread thread = new Thread(new Runnable(){
	        		    @Override
	        		    public void run() {
	        		        try {
	        		           if (server != null)
	        		           {
	        		        	   server.postConnection("statusnode1", "no");
	        		           } else
	        		           {
	        		        	   //Toast.makeText(getApplicationContext(), "server==null", Toast.LENGTH_SHORT).show();
	        		        	   //Intent intent = new Intent(getApplicationContext(), ServerService.class);
	        		               //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	        		               //startService(intent);
	        		           }
	        		        	
	        		    		    }
	        		    		catch (Exception e)
	        		    		{ 	e.printStackTrace();
	        		    			Log.i("postNode -server:", server.toString());
	        		    			Log.i("postNodeException: ", e.getStackTrace()[0].toString());	
	        		    		
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
	        	
	     		  
	     			private Runnable repeater = new Runnable(){
	     		        public void run(){
	     		            post();
	     		            if (MainActivity.NodeOneWon){
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
