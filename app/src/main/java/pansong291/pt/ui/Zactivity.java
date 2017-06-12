package pansong291.pt.ui;

import android.app.Activity;
import android.os.Bundle;
import pansong291.crash.ActivityControl;
import android.widget.Toast;

public class Zactivity extends Activity
{
 android.text.ClipboardManager mcbm;
 @Override
 protected void onResume()
 {
  super.onResume();
  
 }
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ActivityControl.getActivityControl().addActivity(this);
  
  mcbm=(android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ActivityControl.getActivityControl().removeActivity(this);
 }
 
 public void toast(String s)
 {
  toast(s,0);
 }
 
 public void toast(String s,int i)
 {
  Toast.makeText(this,s,i).show();
 }
 
}
