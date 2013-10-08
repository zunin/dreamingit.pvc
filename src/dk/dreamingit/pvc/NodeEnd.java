package dk.dreamingit.pvc;

import android.media.MediaPlayer;
import android.view.View;

public class NodeEnd extends EmptyNode {

	private boolean win = true;
	private MediaPlayer mediaPlayer;
	
	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inflateNarrative() {
		

		if (team.equals("USA"))
		{
			if(win)
			{
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.usaanthem);
				setStory(R.string.usa_endwin);	
			} else
			{
				setStory(R.string.usa_endlose);
			}
		} else
		{
			if(win)
			{
				setStory(R.string.ussr_endwin);	
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ussranthem);
			} else
			{
				setStory(R.string.ussr_endlose);
			}
		}
		
		if(!win)
		{
			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sadsongforbrokenhearts);
		}
		mediaPlayer.start();
		
	}

}
