package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeMapIntro extends EmptyNode {

	@Override
	protected void inflateNarrative() {
		setStory(R.string.shared_map_intro);
	}

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(MainActivity.class);
		//intent.putExtra(NODE_NAME, value)
		startActivity(intent);

	}



}
