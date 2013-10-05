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
		private TextView title, storyView;
		private Resources res;

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_start_node);
			
			res = getResources();
			Intent intent = getIntent();
			
			title = (TextView) findViewById(R.id.node_title);
			storyView = (TextView) findViewById(R.id.node_story);
			
			
			String team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
			title.setText(team);
			
			ArrayList<Integer> story = intent.getIntegerArrayListExtra("story_list");
			storyView.setText(story.iterator().next());
			
			drawMascot(team);
			
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.start_node, menu);
			return true;
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
