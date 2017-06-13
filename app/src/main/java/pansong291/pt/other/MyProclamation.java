package pansong291.pt.other;

import android.app.AlertDialog;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import pansong291.network.Proclamation;
import pansong291.pt.ui.Zactivity;

public class MyProclamation extends Proclamation
{
 private Editor spe;
 private LinearLayout llt;//公告的父容器
 private TextView txtvw;//水平滚动的公告View
 private Zactivity act;

 public MyProclamation(Zactivity a,String u,LinearLayout v)
 {
  super(a,"t.cn/"+u);
  act=a;
  llt=v;
  spe=a.sp.edit();
 }

 public void start()
 {
  new Thread(myRube).start();
 }

 @Override
 protected void thenDo(boolean success)
 {
  if(!success||!shouldS())return;

  //*****此处做个标记，qzGX这个参数应当为整型，以后再作修改

  spe.putBoolean(act.QZGX,getForceUpdata()).commit();
  txtvw=new TextView(act);
  txtvw.setWidth(llt.getWidth());
  txtvw.setClickable(true);
  txtvw.setTextAppearance(act,android.R.style.TextAppearance_Small);
  txtvw.setTextColor(Color.BLACK);
  //跑马灯必要3个属性
  txtvw.setFocusable(true);
  txtvw.setFocusableInTouchMode(true);
  txtvw.setEllipsize(TextUtils.TruncateAt.MARQUEE);

  txtvw.setMarqueeRepeatLimit(-1);//重复次数，-1无限
  txtvw.setSingleLine(true);
  txtvw.setOnClickListener(new OnClickListener()
   {
    @Override
    public void onClick(View p1)
    {
     ScrollView slv=new ScrollView(act);
     TextView ntxt=new TextView(act);
     ntxt.setPadding(20,10,20,10);
     ntxt.setAutoLinkMask(Linkify.WEB_URLS);
     ntxt.setTextSize(16);
     ntxt.setTextColor(Color.BLACK);
     ntxt.setText(String.format(getProclamationMessage()));
     slv.addView(ntxt);
     new AlertDialog.Builder(act)
      .setTitle("公告")
      .setView(slv)
      .setNegativeButton("确定",null)
      .show();
    }
   });
  txtvw.setText(String.format(getProclamationMessage()));
  llt.addView(txtvw);

 }

 //根据时间判断是否显示
 private boolean shouldS()
 {
  if(getLonShowTime()==0)return true;
  return getLonShowTime()-System.currentTimeMillis()>0;
 }
}

