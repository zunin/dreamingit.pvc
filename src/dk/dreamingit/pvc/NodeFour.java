package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeFour extends EmptyNode {

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeFourActivity.class);
		startActivity(intent);
	}

	@Override
	protected void inflateNarrative() {
		if (team.equals("USA"))
		{
			setStory(R.string.usa_4);
		} else
		{
			setStory(R.string.ussr_4);
		}
		
	}

}
