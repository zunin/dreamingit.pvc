package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeOne extends EmptyNode
{

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeMapIntro.class);
		startActivity(intent);
		
	}

	@Override
	protected void inflateNarrative() {
		if (team.equals("USA"))
		{
			setStory(R.string.usa_intro_ham);
		} else
		{
			setStory(R.string.ussr_intro_laika);
		}
		
	}

}
