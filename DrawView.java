package com.lk.draftsman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.lk.draftsman.core.DrawEngine;

class DrawView extends SurfaceView implements SurfaceHolder.Callback {

	private UpdateThread updateThread;
	private DrawEngine engine;
	
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		OnTouchListener listener = new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				engine.touch(motionEvent);
				return true;
			}
		};
		this.setOnTouchListener(listener);
		engine = new DrawEngine(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		updateThread = new UpdateThread(getHolder(), engine);
		updateThread.setRunning(true);
		updateThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

		boolean retry = true;
		updateThread.setRunning(false);
		while (retry) {
			try {
				updateThread.join();
				retry = false;
			} catch (InterruptedException ignored) {}
		}
	}
	
	DrawEngine getEngine() {
		return engine;
	}
	
	public void setEngine(DrawEngine engine) {
		this.engine = engine;
	}
}