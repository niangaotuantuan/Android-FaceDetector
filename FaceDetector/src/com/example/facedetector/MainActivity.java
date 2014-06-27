package com.example.facedetector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.example.facedetector.R;
import android.view.Menu;

public class MainActivity extends Activity {
     private SurfaceView mSurfaceView = null;
     private SurfaceHolder mSurfaceHolder = null;
	
	// Several sample images are provided
	private final int NumSamples = 11;
	private int sampleIdx = -1;
	private int samples[];
	
    public final static String EXTRA_ORIG_IMAGE = "com.example.imagecompressor.ORIG_IMAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
         mSurfaceView = (SurfaceView)findViewById(R.id.surfaceview);
         mSurfaceHolder = mSurfaceView.getHolder();

         
		// get the resource ID of the sample images
		samples = new int[NumSamples];
		samples[0] = R.drawable.p1;
 		samples[1] = R.drawable.p2;
 		samples[2] = R.drawable.p3;
 		samples[3] = R.drawable.p4;
 		samples[4] = R.drawable.p5;
 		samples[5] = R.drawable.p6;
 		samples[6] = R.drawable.p7;
 		samples[7] = R.drawable.p8;
 		samples[8] = R.drawable.p9;
 		samples[9] = R.drawable.p10;
 		samples[10] = R.drawable.pp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// select a sample image
	public void selectImage(View view) {		
		sampleIdx = (sampleIdx + 1) % NumSamples;

		
		// display the image selected
		int imageSample = samples[sampleIdx];
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), imageSample);
		//锁定整个SurfaceView
	    Canvas mCanvas = mSurfaceHolder.lockCanvas();
	    //画图
	    mCanvas.drawBitmap(myBitmap, 0f, 0f, null);
	    //绘制完成，提交修改
	    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
	}
	
	// detect faces in the selected image 
	public void facedetection(View view) {
		if (sampleIdx < 0) // if no sample image is selected
			return;
		
		BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
	    //图片的参数，不设置的话会找不到人脸(把Bitmap转换成FaceDetector的数据格式）
	    bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
	    
		//获取图片资源
		int imageSample = samples[sampleIdx];
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), imageSample,bitmapOption);
		
 
    //假设最多有几张脸         
    int numOfFaces = 10;
    FaceDetector mFaceDetector = new FaceDetector(myBitmap.getWidth(),myBitmap.getHeight(),numOfFaces);
    FaceDetector.Face[] mFace = new FaceDetector.Face[numOfFaces];      
    //获取实际上有多少张脸
    numOfFaces = mFaceDetector.findFaces(myBitmap, mFace);
    Log.v("------------->", ""+numOfFaces);
    
    
    //锁定整个SurfaceView
    Canvas mCanvas = mSurfaceHolder.lockCanvas();
    //画图
    mCanvas.drawBitmap(myBitmap, 0f, 0f, null);
    //绘制完成，提交修改
    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    //重新锁一次
    mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    
    Paint mPaint = new Paint();
    //画笔颜色
    mPaint.setColor(Color.GREEN);
    //选择矩形框
    mPaint.setStyle(Paint.Style.STROKE);
    //线宽
    mPaint.setStrokeWidth(2.0f);
    
   float eyesDistance = 0f;
    
   	//查找人脸的原理是：找眼睛。它返回的人脸数据face。
   	//通过调用public float eyesDistance ()，public void getMidPoint (PointF point)
   	//我们可以得到探测到的两眼间距，以及两眼中心点位置（MidPoint）
    //将所有脸框出来
    for(int i=0;i < numOfFaces;i++)
    {
        PointF eyeMidPoint = new PointF();
        //两眼的中点距离
        mFace[i].getMidPoint(eyeMidPoint);
        //两眼之间的距离
        eyesDistance = mFace[i].eyesDistance();
        
        //锁定整个SurfaceView
      mCanvas = mSurfaceHolder.lockCanvas();
        //画矩形框
        mCanvas.drawRect((int)(eyeMidPoint.x-eyesDistance), 
                         (int)(eyeMidPoint.y-eyesDistance), 
                         (int)(eyeMidPoint.x+eyesDistance), 
                         (int)(eyeMidPoint.y+eyesDistance), 
                          mPaint);
        //绘制完成，提交修改
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        //重新锁
        mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
       
  }    
}
	}
	
