package pansong291.pt.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.widget.Toast;
import pansong291.crash.ActivityControl;

public class Zactivity extends Activity
{
 int VERSION_CODE;
 String VERSION_NAME;
 ClipboardManager mcbm;
 public SharedPreferences sp;
 
 public final String V_CODE="v_code",QZGX="qzGX",CLICK_MOVE="click_move";
 
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
  
  mcbm=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
  
  sp=getSharedPreferences(getPackageName()+"_preferences",0);
  
  try{
   PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(),0);
   VERSION_CODE=pi.versionCode;
   VERSION_NAME=pi.versionName;
  }catch(Exception e)
  {
   Log.e("VersionInfo","Exception",e);    
  }
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
