package dk.dreamingit.pvc;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends Service {
	   // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private String responseString = "";
    private String errorcode = "Errorcode: ", values = "V: ";

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
    	ServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServerService.this;
        }
    }

    public void openConnection()
	{
    	Thread thread = new Thread(new Runnable(){
    	    @Override
    	    public void run() {
    	        try {
    	           
    	        	String url = "http://rumapp.dreamingit.dk/get.php/";
    	    	    //String url = "http://ip.jsontest.com/?callback=showMyIP";	
    	    		
    	    		try{
    	    		  HttpClient httpclient = new DefaultHttpClient();
    	    		    HttpResponse response = httpclient.execute(new HttpGet(url));
    	    		    StatusLine statusLine = response.getStatusLine();
    	    		    Log.i("HttpResponseServerService: ", Integer.toString(statusLine.getStatusCode()));
    	    		    errorcode += statusLine.toString();
    	    		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	    		        ByteArrayOutputStream out = new ByteArrayOutputStream();
    	    		        response.getEntity().writeTo(out);
    	    		        
    	    		        
    	    		        responseString = out.toString();
    	    		        out.close();
    	    		        //..more logic
    	    		    } else{
    	    		        //Closes the connection.
    	    		        response.getEntity().getContent().close();
    	    		        throw new IOException(statusLine.getReasonPhrase());
    	    		    }
    	    		}catch (Exception e)
    	    		{ 	Log.i("jsonConnection", e.getStackTrace()[0].toString());
    	    		errorcode += e.getClass();	
    	    		e.printStackTrace();
    	    		    	}
    	    		    
    	        	
    	        } catch (Exception e) {
    	        	errorcode += e.getClass();
    	            e.printStackTrace();
    	        }
    	    }
    	});

    	thread.start(); 
		
	}
   
    public String getResponse()
    {
    	return responseString;
    }
    
    public String getErrorcode()
    {
    	return errorcode;
    }
    
    public void postConnection(final String name, final String value)
	{
    	Thread thread = new Thread(new Runnable(){
    	    @Override
    	    public void run() {
    	        try {
    	           
    	        	String url = "http://rumapp.dreamingit.dk/post.php/";
    	    		
    	            // Create a new HttpClient and Post Header
    	            HttpClient httpclient = new DefaultHttpClient();
    	            HttpPost httppost = new HttpPost(url);

    	            try {
    	                // Add your data
    	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    	                nameValuePairs.add(new BasicNameValuePair(name, value));
    	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    	                // Execute HTTP Post Request
    	                HttpResponse response = httpclient.execute(httppost);
    	                
    	            } catch (ClientProtocolException e) {
    	            	e.printStackTrace();
    	            	errorcode += e.getClass();
    	            } catch (IOException e) {
    	            	e.printStackTrace();
    	            	errorcode += e.getClass();
    	            }
    	        } catch (Exception e){
    	        	errorcode += e.getClass();
    	        e.printStackTrace();	
    	        }
    	    }
    	      });

    	thread.start(); 
    	try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {	
      return Service.START_NOT_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    
    public void updateAllFlags() throws IsTerribleException
    {
    	openConnection();
    	JSONObject json = new JSONObject();
    	try{
    		json = new JSONObject(responseString);
    	} catch (JSONException e)
    	{
    		json = null;
    		
    	} finally
    	{
    		if (json != null)
    		{
    			setRadarBlownUp(json);
                setNodeOneWon(json);
                setWin(json);
    		}

    	}
        
    }
    
    public void getGameStart()
    {
    	openConnection();
    	JSONObject json = new JSONObject();
    	try{
    		json = new JSONObject(responseString);
    	} catch (JSONException e)
    	{
    		json = null;
    		errorcode += "RSPSTR="+responseString;
    	} finally
    	{
    		if (json != null)
    		{
    			if (jsonIsValue(json, "GameStarted", "no"))
    	    	{
    	    		StartGameActivity.gameStarted = "no";
    	    	} else if (jsonIsValue(json, "GameStarted", "yes"))
    	    	{
    	    		StartGameActivity.gameStarted = "yes";
    	    	} else if (jsonIsValue(json, "GameStarted", "USA"))
    	    	{
    	    		StartGameActivity.gameStarted = "USA";
    	    	} else if (jsonIsValue(json, "GameStarted", "USSR"))
    	    	{
    	    		StartGameActivity.gameStarted = "USSR";
    	    	}
    		}

    	}
    }
    
    public void getOtherTeam()
    {
    	openConnection();
    	JSONObject json = new JSONObject();
    	try{
    		json = new JSONObject(responseString);
    	} catch (JSONException e)
    	{
    		json = null;
    	} finally
    	{
    		if (json != null)
    		{
    			if (jsonIsValue(json, "GameStarted", "USA"))
    	    	{
    	    		WaitingForTeamActivity.gameStarted = "USSR";
    	    	} else if (jsonIsValue(json, "GameStarted", "USSR"))
    	    	{
    	    		WaitingForTeamActivity.gameStarted = "USA";
    	    	}
    		}

    	}
    }
    
    public void setWin(JSONObject json)
    {
    	if (MainActivity.team.equals("USA"))
    	{
        	if (jsonIsValue(json, "NodeEGUSSR", "NotDone") &&
        			jsonIsValue(json, "NodeEGUSA", "NotDone"))
        	{
        		MainActivity.win = true;
        	} else
        	{
        		MainActivity.win = false;
        	}
    	}
    	MainActivity.win = true;
    }
    
    public void setRadarBlownUp(JSONObject json)
    {
    	if (jsonIsValue(json, "Node35USA", "USABlownup") &&
    			MainActivity.team.equals("USA"))
    	{
    	MainActivity.RadarBlownUp = true;
    	} else if (jsonIsValue(json, "Node35USSR", "USSRBlownup") &&
    			MainActivity.team.equals("USSR"))
    	{
    	MainActivity.RadarBlownUp = true;
    	}
    }
    
    public void setNodeOneWon(JSONObject json)
    {
    	if (jsonIsValue(json, "AmIFirstNode1", "yes"))
    	{
    		MainActivity.NodeOneWon = true;
    	} else
    	{
    		MainActivity.NodeOneWon = false;
    	}
    }
    
    public String getValue()
    {
    	return values;
    }
    
    private boolean jsonIsValue(JSONObject json, String name, String value)
    {
    	try{
    		if(name.equals("statusnode35"))
    		{
    			values = json.get(name).toString();
    		}
    		if (json.get(name).equals(value))
    		{
    			return true;
    		}
    	} catch(JSONException e)
    	{e.printStackTrace();}
    	return false;
    }
}

class IsTerribleException extends Exception
{
	public IsTerribleException()
	{
		//
	}
	
 }