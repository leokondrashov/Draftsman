package com.lk.draftsman.core;

import android.graphics.Canvas;

public class Point extends Shape {
	private float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(x, y, 5, isHighlight ? highlight : notHighlight);
	}

	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
