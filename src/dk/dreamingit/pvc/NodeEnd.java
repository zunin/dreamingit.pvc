package dk.dreamingit.pvc;

import android.view.View;

public class NodeEnd extends EmptyNode {

	private boolean win = true;
	
	@Override
	public void goNext(View V) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inflateNarrative() {
		setTitle("");
		
		if (team.equals("USA"))
		{
			if(win)
			{
				setStory(R.string.usa_endwin);	
			} else
			{
				setStory(R.string.usa_endlose);
			}
		} else
		{
			if(win)
			{
				setStory(R.string.ussr_endwin);	
			} else
			{
				setStory(R.string.ussr_endlose);
			}
		}
		
	}

}
