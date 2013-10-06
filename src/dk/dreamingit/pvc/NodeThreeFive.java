package dk.dreamingit.pvc;

import android.view.View;

public class NodeThreeFive extends EmptyNode {

	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inflateNarrative() {
		setTitle("");
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_35);
		} else
		{
			setStory(R.string.ussr_35);
		}
		
	}

}
