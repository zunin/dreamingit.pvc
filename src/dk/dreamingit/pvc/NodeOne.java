package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class NodeOne extends EmptyNode
{

	@Override
	public void goNext(View V) {
		Intent intent = createNextIntent(MainActivity.class);
		startActivity(intent);
		
	}

}
