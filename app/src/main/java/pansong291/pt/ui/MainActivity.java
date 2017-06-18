package pansong291.pt.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import pansong291.pt.R;
import pansong291.pt.other.MultiClick;
import pansong291.pt.other.MyProclamation;
import pansong291.pt.other.MyUpdata;
import pansong291.pt.other.MyUpdataDialogListener;
import pansong291.pt.view.SelectView;

public class MainActivity extends Zactivity
{
 int VERSION_CODE;
 String VERSION_NAME;
 
 ImageView mimg;
 SelectView msv;
 LinearLayout mLayout1,mLayout2;
 TextView mtxtx,mtxty,mtxta,mtxtr,mtxtg,mtxtb,mtxtc;
 View mmenu;
 Bitmap mbitmap;
 MultiClick mck;
 ToggleButton mtb1,mtb2;
 private float screenW,screenH;//屏幕宽高
 private float statusBarH=0f;//状态栏的高度
 private String Pcpath="无";//图片路径
 private String Pcname="无";//图片名称
 private String Pcsize;//图片大小
 
 //图片缩放移动变量
 static final int NONE=0;
 static final int DRAG=1;//拖动中
 static final int ZOOM=2;//缩放中
 private int mode=NONE;//当前的事件
 private float scale;//缩放的比例 X Y方向都是这个值 越大缩放的越快
 private float beforeLenght;//两触点距离
 private float afterLenght;//两触点距离
 private Matrix matrix;//用于移动缩放的矩阵
 private Matrix savedMat;//保存现状
 private Matrix originalMat;//记录初始值
 private PointF start_Point;//开始点击的触点
 private PointF mid_Point;//记录两触点的中点
 
 //SelectView变量
 private PointF down_Point;//开始点击的触点
 private PointF img_Point;//图片上的触点
 
 @Override
 public void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  if(Build.VERSION.SDK_INT<11)
   requestWindowFeature(Window.FEATURE_NO_TITLE);
  //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
   //WindowManager.LayoutParams.FLAG_FULLSCREEN);
  setContentView(R.layout.activity_main);
  // init();实例化对象
  //获取屏幕宽高
  screenW=getWindowManager().getDefaultDisplay().getWidth();//屏幕宽（像素，如：480px）
  screenH=getWindowManager().getDefaultDisplay().getHeight();//屏幕高（像素，如：800px）
  statusBarH=getStatusHeight(this);
  mmenu=findViewById(R.id.mMenu1);
  mimg=(ImageView)findViewById(R.id.mImageView1);
  msv=(SelectView)findViewById(R.id.mImageView2);
  mLayout1=(LinearLayout)findViewById(R.id.mLayout1);
  mLayout2=(LinearLayout)findViewById(R.id.mLayout2);
  mtxtx=(TextView)findViewById(R.id.mTVx);
  mtxty=(TextView)findViewById(R.id.mTVy);
  mtxta=(TextView)findViewById(R.id.mTVa);
  mtxtr=(TextView)findViewById(R.id.mTVr);
  mtxtg=(TextView)findViewById(R.id.mTVg);
  mtxtb=(TextView)findViewById(R.id.mTVb);
  mtxtc=(TextView)findViewById(R.id.mTVc);
  mtb1=(ToggleButton)findViewById(R.id.mSuoDing);
  mtb2=(ToggleButton)findViewById(R.id.mYDorDJ);
  start_Point=new PointF();
  mid_Point=new PointF();
  matrix=new Matrix();//注意，这里是3个实例
  savedMat=new Matrix();//不是一个
  originalMat=new Matrix();//否则只是一个实例，而名字不同罢了
  down_Point=new PointF();
  img_Point=new PointF();
  mck=new MultiClick(){
   @Override
   protected void onClick()
   {
	setImgFitCenter();
   }
  };
  
  try{
   PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(),0);
   VERSION_CODE=pi.versionCode;
   VERSION_NAME=pi.versionName;
  }catch(Exception e)
  {
  }
  
  int oldVerCode=sp.getInt(V_CODE,99999999);
  if(oldVerCode<VERSION_CODE)
  {
   //用户更新了本应用
   new AlertDialog.Builder(this)
    .setTitle("版本升级")
    .setMessage(String.format("感谢您对本应用的支持！\n本应用已成功升级到%1$s版本。\n%2$s",VERSION_NAME,getString(R.string.update_msg)))
    .setPositiveButton("确定",null)
    .show();
  }else if(oldVerCode==99999999)
  {
   //用户第一次安装本应用
   new AlertDialog.Builder(this)
    .setTitle("声明")
    .setMessage(String.format("感谢您安装本应用！\n%s",getString(R.string.hello_user)))
    .setPositiveButton("确定",null)
    .show();
  }
  sp.edit().putInt(V_CODE,VERSION_CODE).commit();

  //公告相关
  LinearLayout llt=(LinearLayout)findViewById(R.id.main_gg);
  new MyProclamation(this,"RyOATtQ",llt).start();

  //更新相关
  if(sp.getBoolean("自动检查更新的选项",true)||sp.getBoolean(QZGX,false))
   new MyUpdata(this,"RyOAlwR",new MyUpdataDialogListener(this)).checkNow(false,null);
   
  try{
   Pcpath=getIntent().getData().getPath();
   Pcname=Pcpath.substring(Pcpath.lastIndexOf("/")+1);
   mbitmap=BitmapFactory.decodeFile(Pcpath);
  }catch(Exception e)
  {
   mbitmap=getmBitmap();
  }
  mimg.setImageBitmap(mbitmap);
  originalMat.set(mimg.getImageMatrix());
  setImgFitCenter();
  setLayoutPosition(0,0,0,0);
  Pcsize=mbitmap.getWidth()+"*"+mbitmap.getHeight();
  mtb2.setChecked(sp.getBoolean(CLICK_MOVE,false));
 }

 public void setImgFitCenter()
 {
  float bitH=mbitmap.getHeight(),
   bitW=mbitmap.getWidth();
  scale=bitW/bitH<screenW/(screenH-statusBarH)?
   (screenH-statusBarH)/bitH:screenW/bitW;
  matrix.set(originalMat);
  matrix.postTranslate((screenW-bitW)/2,(screenH-statusBarH-bitH)/2);
  matrix.postScale(scale,scale,screenW/2,(screenH-statusBarH)/2);
  mimg.setImageMatrix(matrix);
 }
 
 int a,r,g,b,tc;
 public void setLayoutPosition(int zx,int zy,int w,int h)
 {
  zx+=15;zy-=15;
  if(zx>screenW/2)zx-=mLayout1.getWidth()+30;
  if(zy<(screenH-statusBarH)/2)zy+=mLayout1.getHeight()+30;
  mLayout1.layout(zx,zy-mLayout1.getHeight(),zx+mLayout1.getWidth(),zy);
  if(w>=0&&w<mbitmap.getWidth()
   &&h>=0&&h<mbitmap.getHeight())
  {
   int pixelColor=mbitmap.getPixel(w,h);
   a=Color.alpha(pixelColor);
   r=Color.red(pixelColor);
   g=Color.green(pixelColor);
   b=Color.blue(pixelColor);
   mtxtx.setText("x="+(w+1));
   mtxty.setText("y="+(h+1));
   mtxta.setText("a="+a);
   mtxtr.setText("r="+r);
   mtxtg.setText("g="+g);
   mtxtb.setText("b="+b);
   mtxtc.setText("c="+getHexC(a,r,g,b));
   setTextColor(r,g,b);
   mLayout2.setBackgroundColor(pixelColor);
  }else
  {
   mtxtx.setText("");
   mtxty.setText("");
   mtxta.setText("");
   mtxtr.setText("脱离图片范围");
   mtxtg.setText("");
   mtxtb.setText("");
   mtxtc.setText("");
  }
 }
 
 private String getHexC(int a,int r,int g,int b)
 {
  /***
  String s=Integer.toHexString(b);
  if(s.length()==1)
   s="0"+s;
  s=Integer.toHexString(g)+s;
  if(s.length()==3)
   s="0"+s;
  s=Integer.toHexString(r)+s;
  if(s.length()==5)
   s="0"+s;
  s=Integer.toHexString(a)+s;
  if(s.length()==7)
   s="0"+s;
  /***/
  String s="";
  int i1=0;
  for(int i=0;i<4;i++)
  {
   switch(i)
   {
	case 0:i1=b;break;
    case 1:i1=g;break;
    case 2:i1=r;break;
    case 3:i1=a;break;
   }
   s=Integer.toHexString(i1)+s;
   if(s.length()%2!=0)s="0"+s;
  }
  return s;
  
 }
 
 private void setTextColor(int r,int g,int b)
 {
  if(r>102&&r<153)r=0;
  if(g>102&&g<153)g=0;
  if(b>102&&b<153)b=0;
  tc=Color.rgb(255-r,255-g,255-b);
  mtxtx.setTextColor(tc);
  mtxty.setTextColor(tc);
  mtxta.setTextColor(tc);
  mtxtr.setTextColor(tc);
  mtxtg.setTextColor(tc);
  mtxtb.setTextColor(tc);
  mtxtc.setTextColor(tc);
 }
 
 //处理触碰
 /*
  ACTION_MASK在Android中是应用于多点触摸操作，字面上的意思大概是动作掩码的意思吧。
  在onTouchEvent(MotionEvent event)中，
  使用switch(event.getAction())可以处理
  ACTION_DOWN和ACTION_UP事件；
  使用switch(event.getAction()&MotionEvent.ACTION_MASK)
  就可以处理处理多点触摸的ACTION_POINTER_DOWN
  和ACTION_POINTER_UP事件。
  ACTION_DOWN和ACTION_UP就是单点触摸屏幕，按下去和放开的操作；
  ACTION_POINTER_DOWN和ACTION_POINTER_UP就是多点触摸屏幕，
  当有一只手指按下去的时候，另一只手指按下和放开的动作捕捉；
  */
 @Override
 public boolean onTouchEvent(MotionEvent event)
 {
  if(mtb1.isChecked())
   onSelectMove(event);
  else
   moveArate(event);
  return true;
 }
 
 //对SelectView的移动
 public void onSelectMove(MotionEvent me)
 {
  if(mtb2.isChecked())
   msv.setPosition(me.getX(),me.getY()-statusBarH);
  else
  {
   switch(me.getAction())
   {
    case MotionEvent.ACTION_DOWN:
	 down_Point.set(me.getX(),me.getY());
	 break;
    case MotionEvent.ACTION_UP:
	 break;
    case MotionEvent.ACTION_MOVE:
	 msv.postPosition(me.getX()-down_Point.x,me.getY()-down_Point.y);
	 down_Point.set(me.getX(),me.getY());
	 break;
   }
  }
  float[]f1=new float[9];
  //这个f1的值是相对初始的bitmap的
  matrix.getValues(f1);
  //当前触点在位图上的坐标=(十字架坐标-图片坐标)/缩放比例
  img_Point.set((msv.get_x()-f1[2])/f1[0],(msv.get_y()-f1[5])/f1[0]);
  setLayoutPosition((int)msv.get_x(),(int)msv.get_y(),(int)img_Point.x,(int)img_Point.y);
 }
 
 //对图片的移动和缩放处理
 public void moveArate(MotionEvent me)
 {
  switch(me.getAction()&MotionEvent.ACTION_MASK)
  {
   case MotionEvent.ACTION_DOWN:
	if(mck.onMultiClick())
	 break;
	mode=DRAG;
	savedMat.set(mimg.getImageMatrix());
	start_Point.set(me.getX(),me.getY());
	break;
   case MotionEvent.ACTION_POINTER_DOWN:
	if(me.getPointerCount()==2&&spacing(me)>10f)
	{
	 mode=ZOOM;
	 savedMat.set(mimg.getImageMatrix());
	 beforeLenght=spacing(me);
	 mid_Point.set((me.getX(1)+me.getX(0))/2,
	  (me.getY(1)+me.getY(0))/2);
	}
	break;
   case MotionEvent.ACTION_UP:
	mode=NONE;
	break;
   case MotionEvent.ACTION_POINTER_UP:
	mode=NONE;
	break;
   case MotionEvent.ACTION_MOVE:
	//处理拖动
	if(mode==DRAG)
	{
	 //在当前基础上移动
	 matrix.set(savedMat);
	 matrix.postTranslate(
	  me.getX()-start_Point.x,
	  me.getY()-start_Point.y);
	}//处理缩放
	else if(mode==ZOOM&&me.getPointerCount()==2
	 &&(afterLenght=spacing(me))>10f)
	{
	 scale=afterLenght/beforeLenght;
	 matrix.set(savedMat);
	 float[]p=new float[9];
	 matrix.getValues(p);
	 if((p[0]<0.25f&&scale<1f)
	  ||(p[0]>4f&&scale>1f))
	  scale=1f;
	 matrix.postScale(scale,scale,
	  mid_Point.x,mid_Point.y-statusBarH);
	}
	break;
  }
  //设置矩阵
  mimg.setImageMatrix(matrix);
 }
 
 //算两点间的距离
 public float spacing(MotionEvent me)
 {
  float x=me.getX(0)-me.getX(1);
  float y=me.getY(0)-me.getY(1);
  return (float)Math.sqrt(x*x+y*y);
 }
 
 public Bitmap getmBitmap()
 {
  //获取res下的图片
  return BitmapFactory.decodeResource(
   getResources(),
   R.drawable.pic);
  /*
  InputStream is;
  //获取assets目录里的图片流
  try{
   is=getResources().getAssets().open("pic.png");
  }catch(IOException e)
  {
   return null;
  }
  //将图片流转换成Bitmap并返回
  return BitmapFactory.decodeStream(is);
  */
 }
 
 //复制颜色值
 public void onTextClick(View v)
 {
  String ss=((TextView)v).getText().toString();
  if(ss.length()>1)toast(ss);
  if(ss.indexOf("=")!=-1)
   mcbm.setText(mcbm.getText()+"\n"+ss);
 }
 
 //图片详情
 public void onPcMsg(View v)
 {
  mmenu.setVisibility(8);
  if(v.getId()==R.id.mSuoDing)
  {
   if(mtb1.isChecked())
    mLayout1.setVisibility(0);
   else
	mLayout1.setVisibility(8);
  }
  if(v.getId()!=R.id.mPcXiangQ)
   return;
  new AlertDialog.Builder(this)
  .setTitle("图片详情")
  .setMessage("名称: "+Pcname+"\n宽高: "+Pcsize+"\n路径: "+Pcpath)
  .setPositiveButton("复制",new DialogInterface.OnClickListener()
   {@Override
	public void onClick(DialogInterface p1,int p2)
	{
	 mcbm.setText("名称:"+Pcname+"\n大小:"+Pcsize+"\n路径:"+Pcpath);
	 toast("已复制");
	}
  })
  .setNegativeButton("取消",null)
  .show();
 }
 
 //选择图片
 public void onChooseP(View v)
 {
  mmenu.setVisibility(8);
  //使用startActivityForResult是为了获取用户选择的图片
  Intent it=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
  it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
  startActivityForResult(Intent.createChooser(it,"选择图片"),NONE);
 }
 
 //重写onActivityResult以获得你需要的信息
 @Override
 protected void onActivityResult(int requestCode,int resultCode,Intent data)
 {
  super.onActivityResult(requestCode,resultCode,data);
  //此处的requestCode用于判断接收的Activity是不是你想要的那个
  if(resultCode==RESULT_OK&&requestCode==NONE)
  {
   //把现在的Bmp内存释放
   mbitmap.recycle();
   mbitmap=null;
   //获得图片的uri
   Uri uri=data.getData();
   try
   {
    String[]proj={MediaStore.Images.Media.DATA};
    //好像是android多媒体数据库的封装接口，具体的看Android文档
    Cursor cursor=managedQuery(uri,proj,null,null,null);
    //按我个人理解 这个是获得用户选择的图片的索引值
    int column_index=cursor.getColumnIndexOrThrow(proj[0]);
    //将光标移至开头，这个很重要，不小心很容易引起越界
    cursor.moveToFirst();
    //最后根据索引值获取图片路径
    Pcpath=cursor.getString(column_index);
   }catch(Exception e)
   {
    Pcpath=uri.getPath();
   }
   try
   {
    //根据图片路径获取位图
    mbitmap=BitmapFactory.decodeFile(Pcpath);
    Pcname=Pcpath.substring(Pcpath.lastIndexOf("/")+1);
    mimg.setImageBitmap(mbitmap);
    setImgFitCenter();
    Pcsize=mbitmap.getWidth()+"*"+mbitmap.getHeight();
   }catch(Exception e)
   {
    toast(e.getMessage(),1);
   }
  }else if(resultCode==RESULT_OK)
  {
   toast("请重新选择图片");
  }
 }
 
 //获取状态栏高度
 public float getStatusHeight(Activity a1)
 {
  /* 在onCreate()中返回值为0
  Rect localRect=new Rect();
  activity.getWindow().getDecorView()
  .getWindowVisibleDisplayFrame(localRect);
  return localRect.top;
  */
  int i1=0;
  try{
   Class<?>c1=Class.forName("com.android.internal.R$dimen");
   Object o1=c1.newInstance();
   int i2=Integer.parseInt(c1.getField("status_bar_height").get(o1).toString());
   i1=a1.getResources().getDimensionPixelSize(i2);
  }catch(Exception e)
  {
   e.printStackTrace();
  }
  return i1;
 }
 
 boolean shouldShowMenu=true;
 @Override
 public boolean onKeyDown(int keyCode,KeyEvent event)
 {
  if(keyCode==KeyEvent.KEYCODE_MENU)
  {
   if(shouldShowMenu=(mmenu.getVisibility()!=0))
   {
    mmenu.setVisibility(0);
    shouldShowMenu=false;
   }else
   {
    mmenu.setVisibility(8);
    shouldShowMenu=true;
   }
  }else if(keyCode==KeyEvent.KEYCODE_BACK)
  {
   if(mbitmap!=null)
    mbitmap.recycle();
   sp.edit().putBoolean(CLICK_MOVE,mtb2.isChecked()).commit();
   finish();
   System.exit(0);
  }
  return true;
 }
 
 
}
