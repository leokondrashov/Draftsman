package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Shape {

	boolean isHighlight;
	public static Paint highlight = new Paint(), notHighlight = new Paint();

	public abstract void draw(Canvas canvas);

	public void setHighlight(boolean highlight) {
		isHighlight = highlight;
	}
}
