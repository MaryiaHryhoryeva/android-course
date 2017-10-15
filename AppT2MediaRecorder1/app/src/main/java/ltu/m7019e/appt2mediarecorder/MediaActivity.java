package ltu.m7019e.appt2mediarecorder;

import java.io.IOException;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;

public class MediaActivity extends Activity {

	
	 private static final String LOG_TAG = "MediaActivity";
	    private static String mFileName = null;

	    private RecordButton mRecordButton = null;
	    private MediaRecorder mRecorder = null;

	    private PlayButton   mPlayButton = null;
	    private MediaPlayer   mPlayer = null;

		private double startTime = 0;
		private double finalTime = 0;


		private seekBar sbar = null;

	    private void onRecord(boolean start) {

	        if (start) {
	        	AudioRecordTest();
	            startRecording();
	        } else {
	            stopRecording();
	        }
	    }

	    private void onPlay(boolean start) {
	        if (start) {
	        	//AudioRecordTest();
	            startPlaying();
	        } else {
	            //stopPlaying();
	        }
	    }

	    private void startPlaying() {

			new VerySlowTask().execute();
	    }

	    private void stopPlaying() {
            mPlayer.stop();
	        mPlayer.release();
	        mPlayer = null;
	    }

	    private void startRecording() {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setOutputFile(mFileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			try {
				mRecorder.prepare();
			} catch (IOException e) {
				Log.e(LOG_TAG, "prepare() failed");
			}

			mRecorder.start();
	    }

	    private void stopRecording() {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;

	    }

	    class RecordButton extends Button {
	        boolean mStartRecording = true;

	        OnClickListener clicker = new OnClickListener() {
	            public void onClick(View v) {
	          onRecord(mStartRecording);
	                if (mStartRecording) {
	                    setText("Stop recording");
	                } else {
	                    setText("Start recording");
	                }
	                mStartRecording = !mStartRecording;
	            }

	        };

	        public RecordButton(Context ctx) {
	            super(ctx);
	            setText("Start recording");
	            setOnClickListener(clicker);
	        }
	    }

	    class PlayButton extends Button {
	        boolean mStartPlaying = true;

	        OnClickListener clicker = new OnClickListener() {
	            public void onClick(View v) {
	                onPlay(mStartPlaying);
	                if (mStartPlaying) {
	                    setText("Stop playing");
	                } else {
	                    setText("Start playing");
	                }
	                mStartPlaying = !mStartPlaying;
	            }
	        };

	        public PlayButton(Context ctx) {
	            super(ctx);
	            setText("Start playing");
	            setOnClickListener(clicker);
	        }
	    }

	    class seekBar extends SeekBar {
			public seekBar(Context ctx) {
				super(ctx);
			}
		}


	    public void AudioRecordTest() {
	        mFileName = Environment.getExternalStorageDirectory().getPath();
	        mFileName += "/count_down.mp3";
           Log.e(LOG_TAG, "File name: "+mFileName);

	    }

	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);

	        TableLayout ll = new TableLayout(this);
	        mRecordButton = new RecordButton(this);

	        ll.addView(mRecordButton,
	            new LinearLayout.LayoutParams(
	                ViewGroup.LayoutParams.MATCH_PARENT,
	                ViewGroup.LayoutParams.WRAP_CONTENT,
	                0));
	        mPlayButton = new PlayButton(this);
	        ll.addView(mPlayButton,
	        		new LinearLayout.LayoutParams(
	                ViewGroup.LayoutParams.MATCH_PARENT,
	                ViewGroup.LayoutParams.WRAP_CONTENT,
	                0));



			sbar = new seekBar(this);
            //SeekBar sbar = (SeekBar)findViewById(R.id.seekBar);
			ll.addView(sbar,
					new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						0));
			setContentView(ll);


	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        if (mRecorder != null) {
	            mRecorder.release();
	            mRecorder = null;
	        }

	        if (mPlayer != null) {
	            mPlayer.release();
	            mPlayer = null;
	        }
	    }

	private class VerySlowTask extends AsyncTask<String, Integer, Void> {
	public int oneTimeOnly = 0;
		// can use UI thread here
		@Override
		protected void onPreExecute() {

			mFileName = Environment.getExternalStorageDirectory().getPath();
			mFileName += "/count_down.mp3";

			//seekBar = (SeekBar)findViewById(R.id.seekBar);



			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(mFileName);
				mPlayer.prepare();
				sbar.setClickable(false);
				if(oneTimeOnly == 0) {
					sbar.setMax((int)mPlayer.getDuration());
					oneTimeOnly = 1;
				}
				sbar.setProgress(mPlayer.getCurrentPosition());
				mPlayer.start();
			} catch (IOException e) {
				Log.e(LOG_TAG, "prepare() failed");
			}
		}

		// automatically done on worker thread (separate from UI thread)
		@Override
		protected	Void doInBackground(final String... args) {
			int i = 0;
            while (true) {
                i = mPlayer.getCurrentPosition();
                publishProgress(i);
            }
		}

		// periodic updates -it is OK to change UI
		@Override
		protected void onProgressUpdate(Integer... value) {
			sbar.setProgress((int)value[0]);
			//super.onProgressUpdate(value);
			//etMsg.append("\nworking..."+ value[0]);
		}
		@Override
		protected void onPostExecute(final Void unused) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
		}
	}
}
