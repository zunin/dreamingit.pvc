package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

public class NodeFour extends EmptyNode {

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeFourActivity.class);
		startActivity(intent);
	}

	@Override
	protected void inflateNarrative() {
		drawRay();
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_4);
		} else
		{
			setStory(R.string.ussr_4);
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
	
	private void drawRay()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.rayxiong));
	}

}
