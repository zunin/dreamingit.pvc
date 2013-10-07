package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

public class SpeechNode extends EmptyNode
								implements MediaPlayer.OnCompletionListener{

	@Override
	protected void inflateNarrative() {
		setContentView(R.layout.speech_view);
		drawKennedy();
		
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gotomoon);
		mediaPlayer.setOnCompletionListener(this);
		//mediaPlayer.start();
		
		//Skip intro
		onCompletion(mediaPlayer);

	}

	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}
	

	private void drawKennedy()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.kennedy_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.jfk));
	}

	@Override
	public void onCompletion(MediaPlayer mediePlayer) {
		Intent intent = createNextIntent(IntroNode.class);
		startActivity(intent);
		
	}

}
