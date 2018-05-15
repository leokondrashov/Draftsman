package com.lk.draftsman;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.lk.draftsman.core.DrawEngine;

public class UpdateThread extends Thread{

	private final SurfaceHolder surfaceHolder;
	private boolean runFlag = false;
	private long prevTime;
	private DrawEngine engine;

	UpdateThread(SurfaceHolder surfaceHolder, DrawEngine engine){
		this.surfaceHolder = surfaceHolder;
		prevTime = System.currentTimeMillis();
		this.engine = engine;
	}

	@Override
	public void run() {
		long delta;
		while(runFlag) {
			delta = System.currentTimeMillis() - prevTime;
			if(delta > 15) {
				prevTime += delta;
			}
			Canvas canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					engine.draw(canvas);
				}
			}catch (NullPointerException ignored){

			}finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
	
	void setRunning(boolean run) {
		runFlag = run;
	}
}