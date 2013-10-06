/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.dreamingit.pvc;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class MainActivity extends FragmentActivity
										implements
										ConnectionCallbacks,
										OnConnectionFailedListener,
										LocationListener,
										OnMyLocationButtonClickListener {
	
	private GoogleMap mMap;

    private LocationClient mLocationClient;
    private TextView mMessageView;
    
    private ArrayList<String> coordinateList;
    
    private String team;
    private int node;
    
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		String message = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		node = intent.getIntExtra(SelectTeamActivity.CURRENT_NODE, node);
		team = message;
		mMessageView = (TextView) findViewById(R.id.message_text);
		mMessageView.setText(message);
		
		//get those coordinates, get them. real dirty
		Resources res = getResources();
		coordinateList = new ArrayList<String>();
		coordinateList.add(res.getString(R.string.coord_s));
		coordinateList.add(res.getString(R.string.coord_1USA));
		coordinateList.add(res.getString(R.string.coord_1USSR));
		coordinateList.add(res.getString(R.string.coord_15USA));
		coordinateList.add(res.getString(R.string.coord_15USSR));
		coordinateList.add(res.getString(R.string.coord_2));
		coordinateList.add(res.getString(R.string.coord_3USA));
		coordinateList.add(res.getString(R.string.coord_3USSR));
		coordinateList.add(res.getString(R.string.coord_35USA));
		coordinateList.add(res.getString(R.string.coord_35USSR));
		coordinateList.add(res.getString(R.string.coord_4USA));
		coordinateList.add(res.getString(R.string.coord_4USSR));
		coordinateList.add(res.getString(R.string.coord_5));
		coordinateList.add(res.getString(R.string.coord_EUSA));
		coordinateList.add(res.getString(R.string.coord_EUSSR));
		coordinateList.add("56.171794, 10.189998"); //Nygaard
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
            }
            //Put a ring on it - Nygaard
            String coordinate = coordinateList.get(15);
            double latitude = Double.valueOf(coordinate.substring(0, 8));
			double longitude = Double.valueOf(coordinate.substring(10, 18));
			
			addHintOverlay(latitude, longitude, 10);
            
        }
    }

    /**
     * Button to get current Location. This demonstrates how to get the current Location as required
     * without needing to register a LocationListener.
     */
    public void showMyLocation(View view) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            //String msg = "Location = " + mLocationClient.getLastLocation();
        	Location location = mLocationClient.getLastLocation();
        	String msg = "";
        	for (String coordinate : coordinateList)
    		{
    			double latitude = Double.valueOf(coordinate.substring(0, 8));
    			double longitude = Double.valueOf(coordinate.substring(10, 18));
    			
    			if (location != null)
    			{
    				Location curLoc = new Location("Current Location");
    				curLoc.setLatitude(latitude);
    				curLoc.setLongitude(longitude);
    				
    				if (location.distanceTo(curLoc) < 100)
    				{
    					msg += " #" + coordinateList.indexOf(coordinate) + ": " + location.distanceTo(curLoc);
        				msg += " Acc: " + location.getAccuracy() + "m ";
    				}
    				
    			}
    			
        	
    		}
        	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
            
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		for (String coordinate : coordinateList)
		{
			double latitude = Double.valueOf(coordinate.substring(0, 8));
			double longitude = Double.valueOf(coordinate.substring(10, 18));
			Location nodeLoc = new Location("Node Location");
			nodeLoc.setLatitude(latitude);
			nodeLoc.setLongitude(longitude);
			
			if( location.distanceTo(nodeLoc) < 15)
			{
				Toast.makeText(this, "YOU ARE AT A POST, DAMNIT", Toast.LENGTH_SHORT).show();
				
				Intent nextIntent = new Intent(this, NodeTwo.class);
				nextIntent.putExtra(SelectTeamActivity.CURRENT_NODE, node);
				nextIntent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
				startActivity(nextIntent);
			}
			
			
		}
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Don't do anything
		
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		 mLocationClient.requestLocationUpdates(
	                REQUEST,
	                this);  // LocationListener
		
	}

	@Override
	public void onDisconnected() {
		// Do nothing
		
	}
	
	private void addHintOverlay(double latitude, double longitude, int radius)
	{
		Random r = new Random();
		double rLati = offsetLatitude(r.nextInt(radius));
		double rLong = offsetLonitude(r.nextInt(radius), longitude);
		
		if (r.nextInt(2) == 1)
		{
			rLati = rLati * -1;
		}
		if (r.nextInt(2) == 1)
		{
			rLong = rLong * -1;
		}
		
		
		mMap.addCircle(new CircleOptions()
		.center(new LatLng(latitude+rLati, longitude+rLong))
		.radius(radius)
		.strokeColor(Color.BLACK)
		.strokeWidth((float) 5)
		.fillColor(Color.argb(150, 0, 0, 0)));
	}
	
	private double offsetLatitude(int meters)
	{
		
		return 0.0000449 * (meters/5);
	}
	
	private double offsetLonitude(int meters, double latitude)
	{
		return (meters/5) * (0.0000449/(java.lang.Math.cos(latitude)));
		
	}
	
	private Class getLocationNode(String coordinate)
	{
		switch(coordinateList.indexOf(coordinate))
		{
			case 0:
				return IntroNode.class;		//S: Shared
			case 1:
				return NodeOne.class;		//1: USA
			case 2:
				return NodeOne.class;		//1: USSR
			case 3:
				return NodeOneFive.class;	//15: USA
			case 4:
				return NodeOneFive.class;	//15: USSR
			case 5:
				return NodeTwo.class;		//2: Shared
			case 6:
				return NodeThree.class;		//3: USA
			case 7:
				return NodeThree.class;		//3: USSR
			case 8:
				return NodeThreeFive.class;	//35: USA
			case 9:
				return NodeThreeFive.class;	//35: USSR
			case 10:
				return NodeFour.class;		//4: USA
			case 11:
				return NodeFour.class;		//4: USSR
			case 12:
				return NodeFive.class;		//5: Shared
			case 13:
				return NodeEnd.class;		//End: USA
			case 14:
				return NodeEnd.class;		//End: USSR
			default:
				break;
		}
		return MainActivity.class;			//Own class
	}

}
