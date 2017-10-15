package ltu.m7019e.appt6intents;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*Intent intent= new Intent( Intent.ACTION_SENDTO, Uri.parse("smsto:555-1234"));
		intent.putExtra("sms_body", "remember to buy bread and milk");
		startActivity(intent);*/

		
		Intent myActivity2 =
		new Intent(android.content.Intent.ACTION_VIEW);
		Uri data = Uri.parse("file:///sdcard/count_down.mp3"); //change path to my file
		String type = "audio/mp3";
		myActivity2.setDataAndType(data, type);
		startActivity(myActivity2);

		showSoundTracks();
		
	}

	private void showSoundTracks() {
		Intent myIntent= new Intent();
		myIntent.setType("audio/mp3");
		myIntent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(myIntent, 0);
	}//showSoundTracks
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if((requestCode== 0) && (resultCode== Activity.RESULT_OK)) {
			String selectedSong= intent.getDataString();
			Toast.makeText(this, selectedSong, 1).show();
			// play the selected song
			Intent myActivity2 =
					new Intent(android.content.Intent.ACTION_VIEW);
			Uri data = Uri.parse(selectedSong);
			String type = "audio/mp3";
			myActivity2.setDataAndType(data, type);
			startActivity(myActivity2);		
		}
		
	}//onActivityResult
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
