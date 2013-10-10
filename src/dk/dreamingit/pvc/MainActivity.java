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
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
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
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private ServerService server;
    
    public static String team;
    public static int voiceScore = 0;
    private String node;
    
    public static boolean ThreeFiveChosen = false;
    public static boolean NodeOneWon = false;
    public static boolean NodeOneFirst = false;
    public static boolean win = false;
    public static boolean RadarBlownUp = false;
    private boolean firstTimeChange = true;
    
    protected PowerManager.WakeLock mWakeLock;
    private static final LatLng UNIPARKEN = new LatLng(56.168070, 10.204603);
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
		team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		node = intent.getStringExtra(SelectTeamActivity.CURRENT_NODE);
		
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        
		
		//Get the coordinates
		Resources res = getResources();
		coordinateList = new ArrayList<String>();
		coordinateList.add(res.getString(R.string.coord_s));		//0
		coordinateList.add(res.getString(R.string.coord_1USA));		//1
		coordinateList.add(res.getString(R.string.coord_1USSR));	//2
		coordinateList.add(res.getString(R.string.coord_15USA));	//3
		coordinateList.add(res.getString(R.string.coord_15USSR));	//4
		coordinateList.add(res.getString(R.string.coord_2));		//5
		coordinateList.add(res.getString(R.string.coord_3USA));		//6
		coordinateList.add(res.getString(R.string.coord_3USSR));	//7
		coordinateList.add(res.getString(R.string.coord_35USA));	//8
		coordinateList.add(res.getString(R.string.coord_35USSR));	//9
		coordinateList.add(res.getString(R.string.coord_4USA));		//10
		coordinateList.add(res.getString(R.string.coord_4USSR));	//11
		coordinateList.add(res.getString(R.string.coord_5USA));		//12
		coordinateList.add(res.getString(R.string.coord_5USSR));	//13
		coordinateList.add(res.getString(R.string.coord_EndShared));//14
		//coordinateList.add("56.171794, 10.189998"); //Nygaard
		//coordinateList.add("56.170937, 10.190135"); //Hjørnet af StorCenter Nord
		
		//addHintOverlay(56.170937, 10.190135, 1); //SCN - start
		//addHintOverlay(56.169727, 10.189641, 1); //SCN - slut

		//String nygaard = "56.171794, 10.189998";
		//coordinateList.add(8, nygaard);
		//coordinateList.add(9, nygaard);
		//String debugcoord = "56.168775, 10.196905";
		//coordinateList.add(1, debugcoord);
		//coordinateList.add(2, debugcoord);
		
		//onResume stuff
		setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
        
        // Bind to LocalService
        Intent serviceIntent = new Intent(this, ServerService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        
	}
	/**
	 * * START SERVICE
	 */
	  @Override
	    protected void onStart() {
	        super.onStart();

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
    protected void onResume() {
        super.onResume();
        //bindService(new Intent(this, ServerService.class), mConnection, Context.BIND_AUTO_CREATE);

        
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
        setNextStoryMarker();
    }

    @Override
    public void onPause() {
        super.onPause();
        	unbindService(mConnection);
	    
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }
	/**
	 * END SERVICE
	 * */
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UNIPARKEN, 17.0f));
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
            }
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
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		//Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
		//server.openConnection();
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
		try{
		server.updateAllFlags();
		} catch (IsTerribleException e)
		{e.printStackTrace();}
		if (firstTimeChange){
			firstTimeChange=false;
			return;
		}
		
		for (String coordinate : coordinateList)
		{
			
			//Create a new location with coordinates from Nodes
			double latitude = Double.valueOf(coordinate.substring(0, 8));
			double longitude = Double.valueOf(coordinate.substring(10, 18));
			Location nodeLoc = new Location("Node Location");
			nodeLoc.setLatitude(latitude);
			nodeLoc.setLongitude(longitude);
			
			//If you are within 5 meters of a post
			if( location.distanceTo(nodeLoc) < 5)
			{	
				if (getLocationNode(coordinate) == null)
				{} else
				{
					Intent nextIntent = new Intent(this, getLocationNode(coordinate));
					nextIntent.putExtra(SelectTeamActivity.CURRENT_NODE, node);
					nextIntent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
					startActivity(nextIntent);
				}
				
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
	{	//Coefficient for 3_%
		int coefficient = 1;
		if (RadarBlownUp)
		{
			coefficient = 3;
		}
		
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
		  .radius(radius*coefficient)   //set radius in meters
		  .fillColor(Color.argb(150, 255, 255, 153))
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
		Intent intent = getIntent();
		String oldNode = intent.getStringExtra(SelectTeamActivity.CURRENT_NODE);
		
		switch(coordinateList.indexOf(coordinate))
		{
			case 0:
				break;		//S: Shared
			case 1:
				if (oldNode.equals("NodeOne"))
				{break;}
				return NodeOne.class;		//1: USA
			case 2:
				if (oldNode.equals("NodeOne"))
				{break;}
				return NodeOne.class;		//1: USSR
			case 3:
				if (oldNode.equals("NodeOneFive"))
				{break;}
				return NodeOneFive.class;	//15: USA
			case 4:
				if (oldNode.equals("NodeOneFive"))
				{break;}
				return NodeOneFive.class;	//15: USSR
			case 5:
				if (oldNode.equals("NodeTwoTwo"))
				{break;}
				return NodeTwo.class;		//2: Shared
			case 6:
				if (oldNode.equals("NodeThreeChoice"))
				{break;}
				return NodeThree.class;		//3: USA
			case 7:
				if (oldNode.equals("NodeThreeChoice"))
				{break;}
				return NodeThree.class;		//3: USSR
			case 8:
				if (oldNode.equals("NodeThreeFive"))
				{break;}
				return NodeThreeFive.class;	//35: USA
			case 9:
				if (oldNode.equals("NodeThreeFive"))
				{break;}
				return NodeThreeFive.class;	//35: USSR
			case 10:
				if (oldNode.equals("NodeFour"))
				{break;}
				return NodeFour.class;		//4: USA
			case 11:
				if (oldNode.equals("NodeFour" ) || oldNode.equals("NodeFourActivity" ))
				{break;}
				return NodeFour.class;		//4: USSR
			case 12:
				if (oldNode.equals("NodeFiveTwo") || oldNode.equals("NodeFive"))
				{break;}
				return NodeFive.class;		//5: Shared
			case 13:
				if (oldNode.equals("NodeFiveTwo") || oldNode.equals("NodeFive"))
				{break;}
				return NodeFive.class;		//End: USA
			case 14:
				if (oldNode.equals("NodeEnd"))
				{break;}
				return NodeEnd.class;		//End: USSR
			case 15:
				break;		//Location: Nygaard
			default:
				break;
		}
		return null;			//Own class
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
			
		} else if (oldNode.equals("NodeOne")) //Go to NodeTwo or NodeOneFive
		{
			if (team.equals("USA"))
			{
				if (NodeOneFirst)
				{
					addHintOverlay(coordinateList.get(5), radius); //Shared2
				} else
				{
					addHintOverlay(coordinateList.get(3), radius); //USA15
				}
			} else //team = USSR
			{
				if (NodeOneFirst)
				{
					addHintOverlay(coordinateList.get(5), radius); //Shared2
				} else
				{
					addHintOverlay(coordinateList.get(4), radius); //USSR15
				}
			} 
				
		}  else if (oldNode.equals("NodeOneFive")) //Go to NodeTwo
		{
			addHintOverlay(coordinateList.get(5), radius); //Shared2
		} else if (oldNode.equals("NodeTwoTwo")) //Go to NodeThree
		{
			if (team.equals("USA"))
			{
				addHintOverlay(coordinateList.get(6), radius); //USA3
			} else //team = USSR
			{
				addHintOverlay(coordinateList.get(7), radius); //USSR3
			}
		} else if (oldNode.equals("NodeThreeChoice"))  //Go to either 3.5 or 4
		{
			int bonusRadius = 0;
			if (voiceScore > 20)
			{
				bonusRadius=0;
			} else
			{
				bonusRadius = 30-voiceScore;
			}
			
			if (team.equals("USA"))
			{
				if (ThreeFiveChosen)
				{
					addHintOverlay(coordinateList.get(8), radius+(bonusRadius)); //USA35
				} else
				{
					addHintOverlay(coordinateList.get(10), radius+(bonusRadius)); //USA4
				}
			} else //team = USSR
			{
				if (ThreeFiveChosen)
				{
					addHintOverlay(coordinateList.get(9), radius+(bonusRadius)); //USSR35
				} else
				{
					addHintOverlay(coordinateList.get(11), radius+(bonusRadius)); //USSR4
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
			//Get there by Activity (geiger)
		} else if (oldNode.equals("NodeFiveTwo")) //Go to End
		{
				addHintOverlay(coordinateList.get(14), radius); //end_shared
		} 
		
	}
	/*
	 *	coordinateList.add(res.getString(R.string.coord_s));		//0
		coordinateList.add(res.getString(R.string.coord_1USA));		//1
		coordinateList.add(res.getString(R.string.coord_1USSR));	//2
		coordinateList.add(res.getString(R.string.coord_15USA));	//3
		coordinateList.add(res.getString(R.string.coord_15USSR));	//4
		coordinateList.add(res.getString(R.string.coord_2));		//5
		coordinateList.add(res.getString(R.string.coord_3USA));		//6
		coordinateList.add(res.getString(R.string.coord_3USSR));	//7
		coordinateList.add(res.getString(R.string.coord_35USA));	//8
		coordinateList.add(res.getString(R.string.coord_35USSR));	//9
		coordinateList.add(res.getString(R.string.coord_4USA));		//10
		coordinateList.add(res.getString(R.string.coord_4USSR));	//11
		coordinateList.add(res.getString(R.string.coord_5USA));		//12
		coordinateList.add(res.getString(R.string.coord_5USSR));	//13
		coordinateList.add(res.getString(R.string.coord_EndShared));//14
	 * */
}
