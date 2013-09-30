package dk.dreamingit.pvc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class StartNode extends Activity {
		private TextView mMessageView ;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_start_node);
			Intent intent = getIntent();
			String message = intent.getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
			mMessageView = (TextView) findViewById(R.id.hello_world_textview);
			mMessageView.setText(message);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.start_node, menu);
			return true;
		}
		
		public void startGameClicked(View v)
		{
			Intent intent = new Intent(this, SelectTeamActivity.class);
			startActivity(intent);
		}

}
