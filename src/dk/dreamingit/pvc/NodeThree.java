package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class NodeThree extends EmptyNode {

	@Override
	public void goNext(View V) {
		startActivity(createNextIntent(NodeThreeActivity.class));
	}

	@Override
	protected void inflateNarrative() {
		drawJT();
		if (team.equals("USA"))
		{
			setStory(R.string.usa_3_1);
		} else
		{
			setStory(R.string.ussr_3_1);
		}
		
	}
	
	private void drawJT()
	{
		ImageView mascotView = (ImageView) findViewById(R.id.mascot_view);
		mascotView.setImageDrawable(getResources().getDrawable(R.drawable.jt));
	}

}
