package dk.dreamingit.pvc;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.content.res.Resources;

public abstract class EmptyNode extends Activity {
		private TextView titleView, storyView;
		private Resources res;
		private int node;
		private String team;

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_start_node);
			
			res = getResources();
			Intent intent = getIntent();
			
			titleView = (TextView) findViewById(R.id.node_title);
			storyView = (TextView) findViewById(R.id.node_story);
			
			node = intent.getIntExtra(SelectTeamActivity.CURRENT_NODE, node);
			team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
			titleView.setText(team + "Node:" + node);
			storyView.setText(SelectTeamActivity.story.get(node));
			
			drawMascot(team);
			
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.start_node, menu);
			return true;
		}
		
		public Intent createNextIntent(Class cls)
		{
			Intent nextIntent = new Intent(this, cls);
			nextIntent.putExtra(SelectTeamActivity.CURRENT_NODE, node+1);
			nextIntent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
			return nextIntent;
		}
		
		private void drawMascot(String team)
		{
			ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
			
			if (mascotView.getDrawable() != null)
			{
				//Do nothing
			}
			else if (team.equals("USA"))
			{
				mascotView.setImageDrawable(res.getDrawable(R.drawable.ham_chimp));
			} else // team = USSR
			{
				mascotView.setImageDrawable(res.getDrawable(R.drawable.laika));
			}
			
		}
		
		public abstract void goNext(View V);

}
