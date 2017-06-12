package pansong291.pt.ui;

import android.os.Bundle;
import android.widget.TextView;
import pansong291.pt.R;
import pansong291.crash.CrashApplication;
import android.widget.EditText;

public class CrashActivity extends Zactivity
{
 EditText merrorlogtxt;
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_crash);
  merrorlogtxt=(EditText)findViewById(R.id.activityerrorTextView1);
  merrorlogtxt.setText(getIntent().getStringExtra(CrashApplication.ERROR_MESSAGE_TAG));
 }

 @Override
 public void onBackPressed()
 {
  super.onBackPressed();
  System.exit(0);
 }
 
 
 
}
