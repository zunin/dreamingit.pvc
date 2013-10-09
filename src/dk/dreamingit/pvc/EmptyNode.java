package dk.dreamingit.pvc;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public abstract class EmptyNode extends Activity {
		private TextView storyView;
		private Resources res;
		protected String team;
		protected PowerManager.WakeLock mWakeLock;
		private Vibrator v;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.empty_node);
			
	        /* This code together with the one in onDestroy() 
	         * will make the screen be always on until this Activity gets destroyed. */
	        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
	        this.mWakeLock.acquire();
			
			res = getResources();
			Intent intent = getIntent();
			
			storyView = (TextView) findViewById(R.id.node_story);
			
			team = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
			
			drawMascot(team);
			
			inflateNarrative();
			
		}
		
	    @Override
	    public void onDestroy() {
	        this.mWakeLock.release();
	        super.onDestroy();
	    }
		
		public String nodeName()
		{
			return this.getClass().getSimpleName();
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
			nextIntent.putExtra(SelectTeamActivity.CURRENT_NODE, nodeName());
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
