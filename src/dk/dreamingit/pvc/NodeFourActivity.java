package dk.dreamingit.pvc;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class NodeFourActivity extends FragmentActivity 
										implements 
										LocationListener,
										ConnectionCallbacks,
										OnConnectionFailedListener{
	private LocationClient mLocationClient;
	private GeigerPlayer player;
	private int delay = 5;
	private Location taskLocation, nodeFourLocation;
	double maxDistance;
	
	private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_four);
		
		//Get data
		Intent intent = getIntent();
		String team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		
		//Setup player
		player = new GeigerPlayer(this);
		player.onStart(delay); //(secDelayPerTic)
		
		setupLocations(team);
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
		double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));
		nodeFourLocation = new Location("nodeFourLocation");
		nodeFourLocation.setLatitude(latitude);
		nodeFourLocation.setLongitude(longitude);
		
		//Setup taskLocation
		coordinate = "";
		if (team.equals("USA"))
		{
			coordinate = getResources().getString(R.string.coord_4endUSA);
		} else //team = USSR
		{
			coordinate = getResources().getString(R.string.coord_4endUSSR);
		}
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
        setUpLocationClientIfNeeded();
        //mLocationClient.connect();
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
		updateDistance(location);
	}
	
	public void updateDistance(Location location)
	{
		double distance = location.distanceTo(taskLocation);
		
		if (distance < maxDistance/5)
		{
			delay = 5;
		} else if (distance < (maxDistance/5) * 2)
		{
			delay = 4;
		} else if (distance < (maxDistance/5) * 3)
		{
			delay = 3;
		} else if (distance < (maxDistance/5) * 4)
		{
			delay = 2;
		} else if (distance < (maxDistance/5) * 5)
		{
			delay = 1;
		}
	}

}
