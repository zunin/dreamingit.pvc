package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;

public class NodeTwo extends EmptyNode
{

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeTwoTwo.class);
		startActivity(intent);
		
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

	@Override
	protected void inflateNarrative() {
		if (team.equals("USA"))
		{
			setStory(R.string.usa_2_1);
		} else
		{
			setStory(R.string.ussr_2_1);
		}
		
	}

}
