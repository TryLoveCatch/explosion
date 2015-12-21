package com.tlc.explosion;

import java.lang.reflect.Field;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ExplosionView extends View{
	
	private Paint mPaint;
	private Circle[][] mArrCircles;
	private float mAnimValue;
	private int mViewW;
	private int mViewH;
	private ValueAnimator mAnim;

	public ExplosionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}
	public ExplosionView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	public ExplosionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ExplosionView(Context context) {
		super(context);
		init();
	}
	
	public void explode(final View pView){
		Rect tRect = new Rect();
		pView.getGlobalVisibleRect(tRect);
		tRect.offset(0, -getStatusBarHeight());
		
		Bitmap tBmp = Bitmap.createBitmap(pView.getWidth(), pView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas tCanvas = new Canvas();
        if (tBmp != null) {
            synchronized (tCanvas) {
            	tCanvas.setBitmap(tBmp);
                pView.draw(tCanvas);
                tCanvas.setBitmap(null); //清除引用
            }
        }
		
		int tW = tRect.width();
		int tH = tRect.height();
		mViewW = tW;
		mViewH = tH;
		int tCountW = tW / Circle.diameter;
		int tCountH = tH / Circle.diameter;
		
		mArrCircles = new Circle[tCountH][tCountW];
		for(int row = 0; row < tCountH; row++){
			for(int column = 0; column < tCountW; column++){
				Circle tCircle = new Circle();
				tCircle.color = tBmp.getPixel(column * Circle.diameter, row * Circle.diameter);
				tCircle.centerX = tRect.left + column * Circle.diameter;
				tCircle.centerY = tRect.top + row * Circle.diameter;
				tCircle.alpha = 1;
				tCircle.radius = Circle.diameter / 2;
				mArrCircles[row][column] = tCircle;
			}
		}
		
		mAnim.removeAllListeners();
		mAnim.addListener(new Animator.AnimatorListener(){

			@Override
			public void onAnimationStart(Animator animation) {
				pView.animate().alpha(0).setDuration(150).start();
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				pView.animate().alpha(1).setDuration(150).start();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				
			}
			
		});
		mAnim.start();
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(mArrCircles!=null){
			for(Circle[] tArr : mArrCircles){
				for(Circle tCircle : tArr){
					tCircle.update(mAnimValue, mViewW, mViewH);
					mPaint.setColor(tCircle.color);
					mPaint.setAlpha((int)(Color.alpha(tCircle.color) * tCircle.alpha));
					canvas.drawCircle(tCircle.centerX, tCircle.centerY, tCircle.radius, mPaint);
				}
			}
		}
		if(mAnim.isRunning())
			invalidate();
	}
	
	private void init(){
		mPaint = new Paint();
		mAnim = ValueAnimator.ofFloat(0f, 1f);
		mAnim.setDuration(1500);
//		mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		mAnim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mAnimValue = (float)animation.getAnimatedValue();
//				postInvalidate();
			}
		});
		
		bindActivity();
	}
	
	private void bindActivity(){
		ViewGroup tRootView = (ViewGroup) ((Activity)getContext()).findViewById(Window.ID_ANDROID_CONTENT);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tRootView.addView(this, lp);
	}

	private int getStatusBarHeight() {
		// Rect rect= new Rect();
		//
		// Window window= ((Activity) context).getWindow();
		// window.getDecorView().getWindowVisibleDisplayFrame(rect);
		// System.out.println(rect.top);
		// return rect.top;

		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = getContext().getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Log.e("...", sbar + "");
		return sbar;
	}
}
