package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

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

	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(this);
		return tmp;
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

	public boolean isNear(Point other) {
		return (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y) < 2500;
	}
}
