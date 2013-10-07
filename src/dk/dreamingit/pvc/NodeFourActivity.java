package dk.dreamingit.pvc;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.os.Bundle;
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
		
		text = (TextView) findViewById(R.id.delay);
		text.setText("Y U NO WERK?");
		
		
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
		/*double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));*/
		
		coordinate = "56.170937, 10.190135"; //Stor Center Nord Hjørne
		//coordinate = "56.171794, 10.189998"; //Nygaard
		
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
		
		coordinate = "56.169727, 10.189641"; //Stor Center Nord Slut
		
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
		//Toast.makeText(getApplicationContext(), "Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
		text.setText("Delay:" + delay + ", Distance:" + location.distanceTo(taskLocation));
		updateDistance(location);
	}
	
	public void updateDistance(Location location)
	{
		double distance = location.distanceTo(taskLocation);
		
		if (distance < maxDistance/5) //0-20%
		{
			if (delay != 1)
			{
				delay = 1;
				//text.setText(delay);
				Toast.makeText(getApplicationContext(), "1Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
				restart();
			}
		} else if (distance < (maxDistance/5) * 2) //20-40%
		{
			if (delay != 2)
			{
				delay = 2;
				//text.setText(delay);
				Toast.makeText(getApplicationContext(), "2Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
				restart();
			}
		} else if (distance < (maxDistance/5) * 3) //40-60%
		{
			if (delay != 3)
			{
				delay = 3;
				//text.setText(delay);
				Toast.makeText(getApplicationContext(), "3Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
				restart();
			}
		} else if (distance < (maxDistance/5) * 4) //60-80%
		{
			if (delay != 4)
			{
				delay = 4;
				//text.setText(delay);
				Toast.makeText(getApplicationContext(), "4Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
				restart();
			}
		} else if (distance < (maxDistance/5) * 5) //80-100%
		{
			if (delay != 5)
			{
				delay = 5;
				//text.setText(delay);
				Toast.makeText(getApplicationContext(), "Distance:" + location.distanceTo(taskLocation), Toast.LENGTH_SHORT).show();
				restart();
			}
		}
	}
	
	private void restart()
	{
		player.onStop();
		player.onStart(delay);
	}
}