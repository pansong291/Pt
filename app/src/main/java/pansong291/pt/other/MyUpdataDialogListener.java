package pansong291.pt.other;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import pansong291.crash.ActivityControl;
import pansong291.pt.ui.Zactivity;

public class MyUpdataDialogListener implements DialogInterface.OnClickListener
{
 Zactivity act;
 MyUpdata upd;

 public MyUpdataDialogListener(Zactivity a)
 {
  act=a;
 }

 public void setUpDa(MyUpdata u)
 {
  upd=u;
 }

 @Override
 public void onClick(DialogInterface p1,int p2)
 {
  switch(p2)
  {
   case -1:
    //act.toast("下载地址为"+upd.getDownloadUrl());
    //调用浏览器打开网页
    act.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(upd.getDownloadUrl())));
   case -2:
    if(act.sp.getBoolean(act.QZGX,false))
    {
     ActivityControl.getActivityControl().finishProgrom();
    }
    break;
//   case -3:
//  sp.edit().putBoolean("checkBox3",false).commit();
//  break;
  }
 }
}

