package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Line extends Shape {

	private Point a, b;

	public Line(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public void draw(Canvas canvas) {
		float y1, y2;
		y1 = (a.getY() - b.getY()) / (a.getX() - b.getX()) * (- a.getX()) + a.getY();
		y2 = (a.getY() - b.getY()) / (a.getX() - b.getX()) * (canvas.getWidth() - a.getX()) + a.getY();
		canvas.drawLine(0, y1, canvas.getWidth(), y2, isHighlight ? highlight : notHighlight);
	}

	@Override
	public void setHighlight(boolean highlight) {
		super.setHighlight(highlight);
		a.setHighlight(highlight);
		b.setHighlight(highlight);
	}

	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(a);
		tmp.add(b);
		return tmp;
	}
}
