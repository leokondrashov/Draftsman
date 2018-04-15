package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public abstract class Shape {

	boolean isHighlight = false;
	public static Paint highlight = new Paint(), notHighlight = new Paint();

	public abstract void draw(Canvas canvas);

	public void setHighlight(boolean highlight) {
		isHighlight = highlight;
	}

	public abstract ArrayList<Point> getPoints();

}
