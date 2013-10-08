package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeOneFive extends EmptyNode
{
	
	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(MainActivity.class);
		startActivity(intent);
		
	}

	@Override
	protected void inflateNarrative() {
		setTitle("");
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_15);
		} else
		{
			setStory(R.string.ussr_15);
		}
		
	}
	
}
