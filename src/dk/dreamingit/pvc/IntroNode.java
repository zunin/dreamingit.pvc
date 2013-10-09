package dk.dreamingit.pvc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class IntroNode extends EmptyNode
{
	
	@Override
	public void goNext(View V) {

		Intent intent = createNextIntent(MascotNode.class);
		startActivity(intent);
		
	}
	
	
	public static InputStream getInputStreamFromUrl(String url) {
		  InputStream content = null;
		  try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    content = response.getEntity().getContent();
		  } catch (Exception e) {
		  }
		    return content;
		}

	@Override
	protected void inflateNarrative() {
		
		setContentView(R.layout.intro_view);
		TextView storyView = (TextView) findViewById(R.id.intro_text);
		
		if (team.equals("USA"))
		{
			storyView.setText(R.string.usa_S);
		} else
		{
			storyView.setText(R.string.ussr_S);
		}
		
	}

}
