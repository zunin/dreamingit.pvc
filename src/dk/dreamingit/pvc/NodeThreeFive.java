package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class NodeThreeFive extends EmptyNode {

	private boolean exploded = false;
	Button btn;
	
	@Override
	public void goNext(View V) {
		if (exploded)
		{
			Intent intent = createNextIntent(MainActivity.class);
			startActivity(intent);
		} else
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
			mediaPlayer.start();
			btn.setText(R.string.node_btn);
			drawExplodedRadar();
			exploded =  true;
			
		}
	}

	@Override
	protected void inflateNarrative() {
		btn = (Button)  findViewById(R.id.node_btn);
		drawRadar();
		btn.setText(R.string.explode_text);
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_35);
		} else
		{
			setStory(R.string.ussr_35);
		}
		
	}
	
	private void drawRadar()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.radar_before));
	}
	
	private void drawExplodedRadar()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.radar_after));
	}

}
