package dk.dreamingit.pvc;

import android.view.View;

public class NodeTwo extends EmptyNode
{

	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void inflateNarrative() {
		setTitle("");
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_2_1);
		} else
		{
			setStory(R.string.ussr_2_1);
		}
		
	}

}
