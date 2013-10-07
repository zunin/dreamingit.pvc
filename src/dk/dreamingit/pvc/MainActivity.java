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

import android.content.ComponentName;
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
import com.google.android.gms.maps.model.Circle;
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
    private String node;
    private boolean ThreeFiveChosen = false;
    
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
		node = intent.getStringExtra(SelectTeamActivity.CURRENT_NODE);
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
		//coordinateList.add("56.171794, 10.189998"); //Nygaard
		coordinateList.add("56.170937, 10.190135"); //Hjørnet af StorCenter Nord
		
		//addHintOverlay(56.170937, 10.190135, 1); //SCN - start
		//addHintOverlay(56.169727, 10.189641, 1); //SCN - slut
		
		//onResume stuff
		setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
        setNextStoryMarker();
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
        }
    }

    /**
     * Button to get current Location. This demonstrates how to get the current Location as required
     * without needing to register a LocationListener.
     */
    public void showMyLocation(View view) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            //String msg = "Location = " + mLocationClient.getLastLocation();
        	/*
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
        	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); */
        	Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
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
		/*
		 //Put a ring on it - Nygaard
        String coordinate = coordinateList.get(15);
        double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));
		
		addHintOverlay(latitude, longitude, 10);
        */
		
		for (String coordinate : coordinateList)
		{
			//Create a new location with coordinates from Nodes
			double latitude = Double.valueOf(coordinate.substring(0, 8));
			double longitude = Double.valueOf(coordinate.substring(10, 18));
			Location nodeLoc = new Location("Node Location");
			nodeLoc.setLatitude(latitude);
			nodeLoc.setLongitude(longitude);
			
			//If you are within 2 meters of a post
			if( location.distanceTo(nodeLoc) < location.getAccuracy()+2)
			{
				
				Intent nextIntent = new Intent(this, getLocationNode(coordinate));
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
	
	private void addHintOverlay(String coordinate, int radius)
	{
		//Translate string into coordinates
		double latitude = Double.valueOf(coordinate.substring(0, 8));
		double longitude = Double.valueOf(coordinate.substring(10, 18));
		
		//Random offset
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
		
		//Draw circle
		LatLng point = new LatLng(rLati+latitude, rLong+longitude);
		// LatLng nygaard = new LatLng(56.170937, 10.190135);
		CircleOptions circleOptions = new CircleOptions()
		  .center(point)   //set center
		  .radius(radius)   //set radius in meters
		  .fillColor(Color.argb(150, 0, 0, 0))
		  .strokeColor(Color.WHITE)
		  .strokeWidth(5);
		  
		  Circle myCircle = mMap.addCircle(circleOptions);
		 /*
		mMap.addCircle(new CircleOptions()
		.center(new LatLng(latitude+rLati, longitude+rLong))
		.radius(radius)
		.strokeColor(Color.BLACK)
		.strokeWidth((float) 5)
		.fillColor(Color.argb(150, 0, 0, 0)));*/
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
			case 15:
				return NodeFourActivity.class;		//Location: Nygaard
			default:
				break;
		}
		return MainActivity.class;			//Own class
	}

	private void setNextStoryMarker()
	{
		if (mMap == null)
		{return;}
		
		Intent intent = getIntent();
		String oldNode = intent.getStringExtra(SelectTeamActivity.CURRENT_NODE);
		int radius = 10;
		
		if (oldNode.equals("NodeMapIntro")) //Go to NodeOne
		{
			if (team.equals("USA"))
			{
				addHintOverlay(coordinateList.get(1), radius); //USA1

				
			} else //team = USSR
			{
				addHintOverlay(coordinateList.get(2), radius); //USSR1
			}
			
		} else if (oldNode.equals("NodeOne")) //Go to NodeTwo
		{
			addHintOverlay(coordinateList.get(5), radius); //Shared2
		} else if (oldNode.equals("NodeTwo")) //Go to NodeThree
		{
			if (team.equals("USA"))
			{
				addHintOverlay(coordinateList.get(6), radius); //USA3

				
			} else //team = USSR
			{
				addHintOverlay(coordinateList.get(7), radius); //USSR3
			}
		} else if (oldNode.equals("NodeThreeActivity"))  //Go to either 3.5 or 4
		{
			if (team.equals("USA"))
			{
				if (ThreeFiveChosen)
				{
					addHintOverlay(coordinateList.get(8), radius); //USA35
				} else
				{
					addHintOverlay(coordinateList.get(10), radius); //USA4
				}
			} else //team = USSR
			{
				if (ThreeFiveChosen)
				{
					addHintOverlay(coordinateList.get(9), radius); //USSR35
				} else
				{
					addHintOverlay(coordinateList.get(11), radius); //USSR4
				}
			}
		} else if (oldNode.equals("NodeThreeFive")) //Go to 4 
		{
			if (team.equals("USA"))
			{
				addHintOverlay(coordinateList.get(10), radius); //USA4

				
			} else //team = USSR
			{
				addHintOverlay(coordinateList.get(11), radius); //USSR4
			}	
		} else if (oldNode.equals("NodeFour")) 
		{
			addHintOverlay(coordinateList.get(12), radius); //Shared5
		} else if (oldNode.equals("NodeFive")) //Go to End
		{
			if (team.equals("USA"))
			{
				addHintOverlay(coordinateList.get(13), radius); //USAEND

				
			} else //team = USSR
			{
				addHintOverlay(coordinateList.get(14), radius); //USSREND
			}	
		} 
		
	}
	
}
