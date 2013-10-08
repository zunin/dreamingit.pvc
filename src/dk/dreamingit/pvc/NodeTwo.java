package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeTwo extends EmptyNode
{

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeTwoTwo.class);
		startActivity(intent);
		
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
