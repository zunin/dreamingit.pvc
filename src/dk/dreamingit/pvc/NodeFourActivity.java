package dk.dreamingit.pvc;

import com.google.android.gms.location.LocationClient;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class NodeFourActivity extends Activity {
	//private LocationClient mLocationClient;
	private AudioManager audioManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_four);
		
		audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        //setUpLocationClientIfNeeded();
        //mLocationClient.connect();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_four, menu);
		return true;
	}
	/*
	private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
            
        }
    } */

}
