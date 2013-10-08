package dk.dreamingit.pvc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

public class NodeThreeActivity extends Activity {
	private static final int REQUEST_CODE = 80085;
	private ArrayList<String> matches, resKeys;
	private String listenBonusText;
	private int index;
	private int score;
	private TextView clueView;
	protected PowerManager.WakeLock mWakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_three);
		// Show the Up button in the action bar.
		setupActionBar();
		
        /* This code together with the one in onDestroy() 
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pwnMan = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pwnMan.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
		
		//VOICE STUFF -disable button if no listener is present
		Button speakButton = (Button) findViewById(R.id.listenButton);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
        
        //List of results
        String[] voiceClues = getResources().getStringArray(R.array.voice_clues);
        resKeys = new ArrayList<String>(Arrays.asList(voiceClues));
        
        clueView = (TextView) findViewById(R.id.voice_clue);
        clueView.setText(resKeys.get(index));
	}
	
	@Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
	
    public void listenButtonClicked(View v)
    {
    	if(index < resKeys.size())
    	{
    		listenBonusText = resKeys.get(index);
        	startVoiceActivity();
    	} else
    	{
    		//Slut aktiviteten
    	}
    	
    }
    
    private void checkResults()
    {
    	//Get key words for answer
    	String[] voiceSolutionKeys = getResources().getStringArray(R.array.voice_keys);
    	ArrayList<String> solutionKeys = new ArrayList<String>(Arrays.asList(voiceSolutionKeys));
    	
    	//Split sentence into words and put them in a list
    	String[] splittedWords = solutionKeys.get(index).split("\\s+");
    	List<String> wordList = Arrays.asList(splittedWords);
    	
    	//Find best sentence
    	int[] sentenceScore = new int[matches.size()];
    	int biggestIndex = 0;
    	
    	
    	for (String sentence : matches)
    	{
    		for (String word : wordList)
    		{
    			if(sentence.contains(word))
    			{
    				sentenceScore[matches.indexOf(sentence)]++;
    			}
    			
    		}
    		if (sentenceScore[matches.indexOf(sentence)] > biggestIndex)
    		{
    			biggestIndex = matches.indexOf(sentence);
    		}
    		
    	}
    	
    	score = score + sentenceScore[biggestIndex];
    }
    
    private void startVoiceActivity()
    {
       	/*
    	 * Required extras:

			EXTRA_LANGUAGE_MODEL
			Optional extras:
			
			EXTRA_PROMPT
			EXTRA_LANGUAGE
			EXTRA_MAX_RESULTS
			EXTRA_RESULTS_PENDINGINTENT
			EXTRA_RESULTS_PENDINGINTENT_BUNDLE
    	 * */
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        
        //EXTRA_PROMT: Optional text prompt to show to the user when asking them to speak.
        //String listenBonusText = getResources().getString(R.string.listenBonusText);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, listenBonusText);
        startActivityForResult(intent, REQUEST_CODE);
    }
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            /*
        	// Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
           */
        	
        	
        	//Get voiceRecognizers results from the internet
        	matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
        	
        	checkResults();
        	index++;
        	//if (index < resKeys.size())
        	if (index < 5)
        	{
        		clueView.setText(resKeys.get(index) + " " + score);
        	} else
        	{
        		// Next activity
        		String team = getIntent().getStringExtra(SelectTeamActivity.EXTRA_MESSAGE);
        		Intent nextIntent = new Intent(this, NodeThreeChoice.class);
    			nextIntent.putExtra(SelectTeamActivity.EXTRA_MESSAGE, team);
    			nextIntent.putExtra(SelectTeamActivity.VOICE_SCORE, score);
    			startActivity(nextIntent);
        	}
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }
    

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_three, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
