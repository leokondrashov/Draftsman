package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Circle extends Shape {
	
	Point c, a;
	
	Circle(Point c, Point a) {
		this.c = c;
		this.a = a;
	}

	@Override
	public void draw(Canvas canvas) {
		float r = (float) Math.sqrt((c.getX() - a.getX()) * (c.getX() - a.getX()) + (c.getY() - a.getY()) * (c.getY() - a.getY()));
		canvas.drawCircle(c.getX(), c.getY(), r, isHighlight ? highlight : notHighlight);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Circle))
			return false;
		Circle s1 = (Circle) obj;
		Point a1 = s1.getPoints().get(0);
		Point b1 = s1.getPoints().get(1);
		Point a2 = this.getPoints().get(0);
		Point b2 = this.getPoints().get(1);
		float r1 = (float) Math.sqrt((a1.getX() - b1.getX()) * (a1.getX() - b1.getX()) + (a1.getY() - b1.getY()) * (a1.getY() - b1.getY()));
		float r2 = (float) Math.sqrt((a2.getX() - b2.getX()) * (a2.getX() - b2.getX()) + (a2.getY() - b2.getY()) * (a2.getY() - b2.getY()));
		return (a1.distance(a2) == 0) && (r1 == r2);
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
	
	@Override
	boolean isNear(Point p) {
		return Math.abs(c.distance(p) - c.distance(a)) < 50;
	}
	
	@Override
	Point nearest(Point p) {
		float pc = p.distance(c);
		float ac = a.distance(c);
		float x0 = (p.getX() - c.getX()) * ac / pc + c.getX();
		float y0 = (p.getY() - c.getY()) * ac / pc + c.getY();
		return new Point(x0, y0);
	}
	
	@Override
	public ArrayList<Point> getGeneralPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(c);
		tmp.add(a);
		return tmp;
	}
}
