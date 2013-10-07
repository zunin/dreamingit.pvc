package dk.dreamingit.pvc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;


public class IntroNode extends EmptyNode
{
	
	@Override
	public void goNext(View V) {
		
		
		Intent intent = createNextIntent(NodeOne.class);
		startActivity(intent);
		
	}

	@Override
	protected void inflateNarrative() {
		
		setContentView(R.layout.intro_view);
		TextView storyView = (TextView) findViewById(R.id.intro_text);
		
		if (team.equals("USA"))
		{
			storyView.setText(R.string.usa_S);
		} else
		{
			storyView.setText(R.string.ussr_S);
		}
		
	}

}
