package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

public class NodeThree extends EmptyNode {

	@Override
	public void goNext(View V) {
		startActivity(createNextIntent(NodeThreeActivity.class));
	}

	@Override
	protected void inflateNarrative() {
		drawJT();
		if (team.equals("USA"))
		{
			setStory(R.string.usa_3_1);
		} else
		{
			setStory(R.string.ussr_3_1);
		}
		
	}
	
	@Override
    protected void onStart() {
        super.onStart();
		//Setup player
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.vednypost);
		mediaPlayer.start();
		//Vibrator
		Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		v.vibrate(1000);
	}
	
	private void drawJT()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.jt));
	}

}
