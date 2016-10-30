package com.pansong.p;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Zactivity extends Activity
{
 android.text.ClipboardManager mcbm;

 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  mcbm=(android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
 }
 
 public void toast(String str,int i)
 {
  Toast.makeText(this,str,i).show();
 }
 public void toast(String str)
 {
  toast(str,0);
 }

}
