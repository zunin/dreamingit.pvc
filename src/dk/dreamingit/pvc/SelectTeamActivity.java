package dk.dreamingit.pvc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectTeamActivity extends Activity {
	// Identifiers
	public final static String EXTRA_MESSAGE = "dk.dreamingit.pvc.TEAM";
	public final static String CURRENT_NODE = "dk.dreamingit.pvc.NODE";
	public final static String VOICE_SCORE = "dk.dreamingit.pvc.VOICE_SCORE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_team);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_team, menu);
		return true;
	}

	public void startUSSR(View v)
	{
 		Intent intent = new Intent(this, NodeFive.class);
		intent.putExtra(EXTRA_MESSAGE, "USSR");
		
		startActivity(intent);
	}
	
	public void startUSA(View v)
	{
		Intent intent = new Intent(this, NodeFive.class);
		intent.putExtra(EXTRA_MESSAGE, "USA");
		
		startActivity(intent);
	}
}
