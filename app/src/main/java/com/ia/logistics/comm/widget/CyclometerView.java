package com.ia.logistics.comm.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ia.logistics.activity.R;

public class CyclometerView extends View {

	private float rotateDegree = 0;
	// 平移矩阵
	private Matrix panTrans ;
	// 平移矩阵
	private Matrix panTrans1 ;
	// 界面需要的图片
	private Bitmap panpic;

	private Bitmap panhandpic;

	private Paint paint;

	private float mDialWidth; //表盘宽度
	private float mDialHeight; //表盘高度

	private float scale = 1;
	public interface OnRadialViewValueChanged {
		public void onValueChanged(int value);
	}
	public CyclometerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		paint.setAntiAlias(true);// 设置画笔无锯齿(如果不设置可以看到效果很差)
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.MyView);
		boolean b = a.getBoolean(R.styleable.MyView_cyclometerBg, false);
		Resources mRes = CyclometerView.this.getResources();
		if (b) {
			panpic = BitmapFactory.decodeStream(mRes
					.openRawResource(R.drawable.right_table));
			panhandpic = BitmapFactory.decodeStream(mRes
					.openRawResource(R.drawable.right_indicator));

		} else {
			panpic = BitmapFactory.decodeStream(mRes
					.openRawResource(R.drawable.left_table));
			panhandpic = BitmapFactory.decodeStream(mRes
					.openRawResource(R.drawable.left_indicator));
		}
		a.recycle();
		initView();
		panTrans = new Matrix();
		panTrans1 = new Matrix();
	}

	private void initView() {
		mDialWidth = panpic.getWidth();
		mDialHeight = panpic.getHeight();
	}

	public void setRotateDegree(float rotateDegree) {
		this.rotateDegree = rotateDegree;
		this.postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);


		canvas.drawBitmap(panpic, panTrans, paint);
		System.out.println("pantrans----------"+panTrans);
//		canvas.translate(155, 170);
//		canvas.rotate(180 * rotateDegree / 1000000 - 90);
//		canvas.drawBitmap(panhandpic, -39, -152, paint);
		panTrans1.postTranslate(116*scale, 18*scale);
		panTrans1.postRotate(180 * rotateDegree / 1000000 - 90, mDialWidth/2*scale, mDialHeight*scale);
		canvas.drawBitmap(panhandpic, panTrans1, paint);
		panTrans1.reset();
		panTrans1.setScale(scale, scale);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if ((w/h) < (mDialWidth/(mDialHeight+37))) {
			scale = w/mDialWidth;
		}else {
			scale = h/(mDialHeight+37);
		}
		panTrans.setScale(scale, scale);
		panTrans1.setScale(scale, scale);
//		rotateDegree = 90;
	}

}
