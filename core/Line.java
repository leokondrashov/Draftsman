package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Line extends Shape {
	
	Point a, b;
	
	Line(Point a, Point b, int color) {
		this.a = a;
		this.b = b;
		this.color = color;
	}

	@Override
	public void draw(Canvas canvas) {
		float k = (a.getY() - b.getY()) / (a.getX() - b.getX());
		float y1, y2, x1, x2;
		y1 = -k * a.getX() + a.getY();
		x1 = 0;
		if (y1 > canvas.getHeight()) {
			y1 = canvas.getHeight();
			x1 = (y1 - a.getY()) / k + a.getX();
		} else if (y1 < 0) {
			y1 = 0;
			x1 = -a.getY() / k + a.getX();
		}
		y2 = k * (canvas.getWidth() - a.getX()) + a.getY();
		x2 = canvas.getWidth();
		if (y2 > canvas.getHeight()) {
			y2 = canvas.getHeight();
			x2 = (y2 - a.getY()) / k + a.getX();
		} else if (y2 < 0) {
			y2 = 0;
			x2 = -a.getY() / k + a.getX();
		}
		paint.setColor(color);
		canvas.drawLine(x1, y1, x2, y2, paint);
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
	
	@Override
	public ArrayList<Point> getGeneralPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(a);
		tmp.add(b);
		return tmp;
	}
}
