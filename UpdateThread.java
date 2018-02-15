package com.lk.draftsman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import com.lk.draftsman.core.DrawEngine;
import com.lk.draftsman.core.Point;
import com.lk.draftsman.core.Shape;

import java.util.ArrayList;

public class UpdateThread extends Thread{

	private final SurfaceHolder surfaceHolder;
	private boolean runFlag = false;
	private long prevTime;
	DrawEngine engine;

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
				canvas = surfaceHolder.lockCanvas(null);
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


	public void setRunning(boolean run) {
		runFlag = run;
	}
}