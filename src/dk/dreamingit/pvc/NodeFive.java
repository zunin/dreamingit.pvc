package dk.dreamingit.pvc;

import android.view.View;

public class NodeFive extends EmptyNode {

	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inflateNarrative() {
		setTitle("");
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_5_1);
		} else
		{
			setStory(R.string.ussr_5_1);
		}
		
	}

}
