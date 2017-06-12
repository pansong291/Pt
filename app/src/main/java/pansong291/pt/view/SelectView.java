package pansong291.pt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SelectView extends ImageView
{
 private Context mct;
 private Paint mPaint;
 private int color;
 private boolean FirstCr=true;
 //线条的位置和间隔
 private float csx=0,csy=0,jg=1.5f;
 //记录初始高宽
 private float h,w;
 //默认构造函数
 public SelectView(Context context)
 {
  super(context);
  mct=context;
  mPaint=new Paint();
 }
 //该构造方法在静态引入XML文件中是必须的
 public SelectView(Context context,AttributeSet paramAttributeSet)
 {
  super(context,paramAttributeSet);
  mct=context;
  mPaint=new Paint();
 }
 
 @Override
 protected void onDraw(Canvas canvas)
 {
  if(FirstCr)
  {
   w=getWidth();
   h=getHeight();
   color=Color.rgb(255,0,0);
   //设置抗锯齿
   mPaint.setAntiAlias(true);
   FirstCr=false;
  }
  mPaint.setColor(color);
  mPaint.setStrokeWidth(1f);

  canvas.drawLine(csx-jg,0,csx-jg,h,mPaint);
  canvas.drawLine(csx+jg,0,csx+jg,h,mPaint);
  canvas.drawLine(0,csy-jg,w,csy-jg,mPaint);
  canvas.drawLine(0,csy+jg,w,csy+jg,mPaint);
 }
 //设置笔刷颜色
 public void setLineaColor(int r,int g,int b)
 {
  color=Color.rgb(r,g,b);
 }
 //设置间隔宽度
 public void setStrokeWidth(int width)
 {
  jg=width;
 }
 //设置位置
 public void setPosition(float x,float y)
 {
  //将位置限定在屏幕内
  if(x>=0&&x<=w)csx=x;
  if(y>=0&&y<=h)csy=y;
  invalidate();
 }
 //移动位置
 public void postPosition(float x,float y)
 {
  csx+=x;
  csy+=y;
  //将位置限定在屏幕内
  if(csx<0||csx>w)csx-=x;
  if(csy<0||csy>h)csy-=y;
  invalidate();
 }
 
 public float get_x()
 {
  return csx;
 }
 
 public float get_y()
 {
  return csy;
 }
  
}
