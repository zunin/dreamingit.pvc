package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeFiveTwo extends EmptyNode {

	@Override
	protected void inflateNarrative() {
	
		if (team.equals("USA"))
		{
			setStory(R.string.usa_5_2);
		} else
		{
			setStory(R.string.ussr_5_2);
		}

	}

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(MainActivity.class);
		startActivity(intent);

	}

}
