package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeThree extends EmptyNode {

	@Override
	public void goNext(View V) {
		startActivity(createNextIntent(NodeThreeActivity.class));
	}

	@Override
	protected void inflateNarrative() {
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_3_1);
		} else
		{
			setStory(R.string.ussr_3_1);
		}
		
	}

}
