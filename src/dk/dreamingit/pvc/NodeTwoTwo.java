package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;

public class NodeTwoTwo extends EmptyNode {

	@Override
	protected void inflateNarrative() {
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
		mediaPlayer.start();
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_2_2);
		} else
		{
			setStory(R.string.ussr_2_2);
		}

	}

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(MainActivity.class);
		startActivity(intent);

	}

}
