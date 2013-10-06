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
		private TextView storyView;
		private Resources res;
		protected String team;

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.empty_node);
			
			res = getResources();
			Intent intent = getIntent();
			
			storyView = (TextView) findViewById(R.id.node_story);
			
			team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);

			
			drawMascot(team);
			
			inflateNarrative();
			
		}
		
		protected abstract void inflateNarrative();

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.start_node, menu);
			return true;
		}
		
		protected void setStory(int NodeNumber)
		{
			
			storyView.setText(NodeNumber);
		}
		
		public Intent createNextIntent(Class cls)
		{
			Intent nextIntent = new Intent(this, cls);
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
