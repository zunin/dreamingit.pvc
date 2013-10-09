package dk.dreamingit.pvc;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class NodeThreeChoice extends Activity {
	private int score = 10;
	private String team;
	private Intent nextIntent;
	protected PowerManager.WakeLock mWakeLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_node_three_choice);
		Intent nextIntent = getIntent();
		//score = nextIntent.getIntExtra(SelectTeamActivity.VOICE_SCORE, 0);
		team = nextIntent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		TextView story = (TextView) findViewById(R.id.three_choice_story);
		
		 /* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
		
		if (team.equals("USA"))
		{
			story.setText(getResources().getString(R.string.usa_3_2));
		} else
		{
			story.setText(getResources().getString(R.string.ussr_3_2));
		}
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
	
	public void clickedYes(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
		intent.putExtra(SelectTeamActivity.CURRENT_NODE, this.getClass().getSimpleName());
		MainActivity.ThreeFiveChosen = true;
		startActivity(intent);
	}
	
	public void clickedNo(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
		intent.putExtra(SelectTeamActivity.CURRENT_NODE, this.getClass().getSimpleName());
		MainActivity.ThreeFiveChosen = false;
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_three_choice, menu);
		return true;
	}

}
