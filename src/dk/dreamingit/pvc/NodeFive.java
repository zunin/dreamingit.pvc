package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class NodeFive extends EmptyNode {

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(NodeFiveTwo.class);
		startActivity(intent);

	}

	@Override
	protected void inflateNarrative() {
		
		if (team.equals("USA"))
		{
			setStory(R.string.usa_5_1);
			drawVonBraun();
		} else
		{
			setStory(R.string.ussr_5_1);
			drawSergei();
		}
		
	}
	
	private void drawSergei()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.sergei));
	}
	
	private void drawVonBraun()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.vonbraun));
	}

}
