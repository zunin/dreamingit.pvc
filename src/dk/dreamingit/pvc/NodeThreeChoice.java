package dk.dreamingit.pvc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class NodeThreeChoice extends Activity {
	private int score;
	private String team;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		score = intent.getIntExtra(SelectTeamActivity.VOICE_SCORE, 0);
		team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_three_choice);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_three_choice, menu);
		return true;
	}

}
