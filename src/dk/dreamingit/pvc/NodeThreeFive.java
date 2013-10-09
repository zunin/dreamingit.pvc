package dk.dreamingit.pvc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class NodeThreeFive extends EmptyNode {

	private boolean exploded = false;
	private ServerService server;
	Button btn;
	
	@Override
	public void goNext(View V) {
		if (exploded)
		{
			Intent intent = createNextIntent(MainActivity.class);
			startActivity(intent);
		} else
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
			mediaPlayer.start();
			btn.setText(R.string.node_btn);
			drawExplodedRadar();
			explode();
			
		}
	}
	
	
	private void explode()
	{
		exploded =  true;
		
		if (team.equals("USA"))
		{
			server.postConnection("statusnode35", "USSRBlownup");
		} else
		{
			server.postConnection("statusnode35", "USABlownup");
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
	
	
	
	@Override
	protected void onPause(){
		super.onPause();
		unbindService(mConnection);
	}
	
	@Override
	protected void inflateNarrative() {
		
		
		btn = (Button)  findViewById(R.id.node_btn);
		drawRadar();
		btn.setText(R.string.explode_text);
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_35);
		} else
		{
			setStory(R.string.ussr_35);
		}
		
	}
	
	private void drawRadar()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.radar_before));
	}
	
	private void drawExplodedRadar()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.radar_after));
	}

}
