package dk.dreamingit.pvc;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SelectTeamActivity extends Activity {
	public final static String EXTRA_MESSAGE = "dk.dreamingit.pvc.TEAM";
	
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
		ArrayList<Integer> USSRStory = new ArrayList<Integer>();
		USSRStory.add(R.string.ussr_S);
		USSRStory.add(R.string.ussr_1);
		USSRStory.add(R.string.ussr_15);
		USSRStory.add(R.string.ussr_2);
		USSRStory.add(R.string.ussr_3);
		USSRStory.add(R.string.ussr_35);
		USSRStory.add(R.string.ussr_4);
		USSRStory.add(R.string.ussr_5);
		USSRStory.add(R.string.ussr_Ewin);
		USSRStory.add(R.string.ussr_Elose);
		
 		Intent intent = new Intent(this, IntroNode.class);
		intent.putExtra(EXTRA_MESSAGE, "USSR");
		intent.putIntegerArrayListExtra("story_list", USSRStory);
		
		startActivity(intent);
	}
	
	public void startUSA(View v)
	{
		ArrayList<Integer> USAStory = new ArrayList<Integer>();
		USAStory.add(R.string.usa_S);
		USAStory.add(R.string.usa_1);
		USAStory.add(R.string.usa_15);
		USAStory.add(R.string.usa_2);
		USAStory.add(R.string.usa_3);
		USAStory.add(R.string.usa_35);
		USAStory.add(R.string.usa_4);
		USAStory.add(R.string.usa_5);
		USAStory.add(R.string.usa_Ewin);
		USAStory.add(R.string.usa_Elose);
		
		Intent intent = new Intent(this, IntroNode.class);
		intent.putExtra(EXTRA_MESSAGE, "USA");
		intent.putIntegerArrayListExtra("story_list", USAStory);
		
		startActivity(intent);
	}
}
