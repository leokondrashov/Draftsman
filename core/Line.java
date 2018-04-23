package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Line extends Shape {

	private Point a, b;
	
	Line(Point a, Point b) {
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
	
	@Override
	boolean isNear(Point p) {
		return distance(p) < 50;
	}
	
	float distance(Point p) {
		float ap = a.distance(p);
		float px = p.getX() - a.getX(), py = p.getY() - a.getY();
		float bx = b.getX() - a.getX(), by = b.getY() - a.getY();
		float ab = a.distance(b);
		float sqrDist = ap * ap - (px * bx + py * by) * (px * bx + py * by) / ab / ab;
		return (float) Math.sqrt(sqrDist);
	}
	
	@Override
	Point nearest(Point p) {
		float px = p.getX() - a.getX(), py = p.getY() - a.getY();
		float bx = b.getX() - a.getX(), by = b.getY() - a.getY();
		float ab = a.distance(b);
		float ao = (px * bx + py * by) / ab;
		float x0 = a.getX() + ao * (b.getX() - a.getX()) / ab;
		float y0 = a.getY() + ao * (b.getY() - a.getY()) / ab;
		return new Point(x0, y0);
	}
}
