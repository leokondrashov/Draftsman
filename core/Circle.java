package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Circle extends Shape {

	private Point c, a;

	public Circle(Point c, Point a) {
		this.c = c;
		this.a = a;
	}

	@Override
	public void draw(Canvas canvas) {
		float r = (float) Math.sqrt((c.getX() - a.getX()) * (c.getX() - a.getX()) + (c.getY() - a.getY()) * (c.getY() - a.getY()));
		canvas.drawCircle(c.getX(), c.getY(), r, isHighlight ? highlight : notHighlight);
	}

	@Override
	public void setHighlight(boolean highlight) {
		super.setHighlight(highlight);
		a.setHighlight(highlight);
		c.setHighlight(highlight);
	}

	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(c);
		tmp.add(a);
		return tmp;
	}
}
