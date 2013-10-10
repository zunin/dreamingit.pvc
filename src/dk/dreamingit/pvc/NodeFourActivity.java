package dk.dreamingit.pvc;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class NodeFourActivity extends FragmentActivity 
										implements 
										LocationListener,
										ConnectionCallbacks,
										OnConnectionFailedListener{
	private LocationClient mLocationClient;
	private GeigerPlayer player;
	private int delay = 5;
	private Location taskLocation, nodeFourLocation;
	private double maxDistance;
	private TextView text;
	private boolean isPlaying = false, found = false;
	private String team;
	private long[] pattern = new long[3];
	private long vibrateDelay; 
	private Vibrator v;
	protected PowerManager.WakeLock mWakeLock;
	
	private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_four);
		
		/* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
		
		//Get data
		Intent intent = getIntent();
		team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		
		//Setup player
		player = new GeigerPlayer(this);
		//player.onStart(delay); //(secDelayPerTic)
		
		//Setup vibrator
		//Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		text = (TextView) findViewById(R.id.delay);
		
		
		setupLocations(team);
	}
	
	@Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
	
	private void setupLocations(String team)
	{
		//Setup nodeFourLocation
		String coordinate = "";
		if (team.equals("USA"))
		{
			coordinate = getResources().getString(R.string.coord_4USA);
		} else //team = USSR
		{
			coordinate = getResources().getString(R.string.coord_4USSR);
		}
		/*double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));*/
		
		//coordinate = "56.170937, 10.190135"; //Stor Center Nord Hjørne
		//coordinate = "56.171794, 10.189998"; //Nygaard
		
		double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));
		
		nodeFourLocation = new Location("nodeFourLocation");
		nodeFourLocation.setLatitude(latitude);
		nodeFourLocation.setLongitude(longitude);
		
		//Setup taskLocation
		coordinate = getResources().getString(R.string.coord_5);
		//coordinate = "56.169727, 10.189641"; //Stor Center Nord Slut
		//coordinate = "56.171794, 10.189998"; //Nygaard
		
		latitude = Double.valueOf(coordinate.substring(0, 8));
		longitude = Double.valueOf(coordinate.substring(10, 18));
		taskLocation = new Location("Task location");
		taskLocation.setLatitude(latitude);
		taskLocation.setLongitude(longitude);
		
		maxDistance = nodeFourLocation.distanceTo(taskLocation);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_four, menu);
		return true;
	}
	
	private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    this.getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
            
        }
    }
	/**
	 *  Stuff from interfaces
	 * */

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		 mLocationClient.requestLocationUpdates(
	                REQUEST,
	                this);  // LocationListener
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		String distanceMessage = getResources().getString(R.string.node_four_distance);
		text.setText(distanceMessage + location.distanceTo(taskLocation));
		updateDistance(location);
	}
	
	public String nodeName()
	{
		return this.getClass().getSimpleName();
	}
	
	public void updateDistance(Location location)
	{
		double distance = location.distanceTo(taskLocation);
		
		if (distance < 7 && found == false
				&& location.getAccuracy() < 20)
		{
			found = true;
			player.onDestroy();
			if (v != null)
			{
				v.cancel();
			}
			
			Intent intent = new Intent(this, NodeFive.class);
			intent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
			intent.putExtra(SelectTeamActivity.CURRENT_NODE, nodeName());
			startActivity(intent);

		} else
		
		if (distance < maxDistance/6) 
		{
			if (delay != 1)
			{
				delay = 1;
				restart();
			}
		} else if (distance < (maxDistance/6) * 2)
		{
			if (delay != 2)
			{
				delay = 2;
				restart();
			}
		} else if (distance < (maxDistance/6) * 3)
		{
			if (delay != 3)
			{
				delay = 3;
				restart();
			}
		} else if (distance < (maxDistance/6) * 4) 
		{
			if (delay != 4)
			{
				delay = 4;
				restart();
			}
		} else if (distance < (maxDistance/6) * 5)
		{
			if (delay != 5)
			{
				delay = 5;
				restart();
			}
		}
	}
	
	private void restart()
	{
		vibrateDelay = delay*1000;
		
		pattern[0] = 0;
		//pattern[1] = 967;
		pattern[1] = 500;
		pattern[2] = vibrateDelay;
		
		if(isPlaying)
		{
			if (v != null)
			{
				v.cancel();
				v.vibrate(pattern, 0);
			}
			player.onStop();
			player.onStart(delay);

			
		} else
		{
			
			
			player.onStart(delay);
			isPlaying = true;
			if (v != null)
			{
				v.vibrate(pattern, 0);
			}
		}
	}
}