package dk.dreamingit.pvc;

import android.content.Intent;
import android.view.View;

public class IntroNode extends EmptyNode
{

	@Override
	public void goNext(View V) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}

}
