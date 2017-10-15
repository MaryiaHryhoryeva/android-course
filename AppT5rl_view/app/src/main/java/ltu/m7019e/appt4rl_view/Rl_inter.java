package ltu.m7019e.appt4rl_view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Rl_inter extends Activity {

	private TextView State;
	private AutoCompleteTextView EditState;
	private Button Submitbtn;
	private ToggleButton toggle;	
	private boolean HOLD=false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		State= (TextView)findViewById(R.id.txtState);
		EditState= (AutoCompleteTextView)findViewById(R.id.edtState);
		Submitbtn = (Button) findViewById(R.id.btnSbm);
		toggle = (ToggleButton) findViewById(R.id.tglbtn);

		String[] states = getResources().getStringArray(R.array.states_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
		EditState.setAdapter(adapter);

		Submitbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			String text;
			text=EditState.getText().toString();
			if(!HOLD){
			// Case when ToggleButton is off	
				State.setText(text);
				EditState.setText("");				
			}			
			else{
			// Case when ToggleButton is clicked
				Context context = getApplicationContext();
				CharSequence textToast = "Status is held";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, textToast, duration);
				toast.show();
			}
				
			}
		});
		
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The toggle is enabled
					EditState.setEnabled(false);
		        	HOLD=true;
		        	
		        } else {
		            // The toggle is disabled
					EditState.setEnabled(true);
		        	HOLD=false;
		        }
		    }
		});
	}


}
