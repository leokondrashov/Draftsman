package com.lk.draftsman.core;

import java.util.ArrayList;

public class PerpLine extends Line {
	
	private Point p1, p2, p3;
	
	PerpLine(Point p1, Point p2, Point p3, int color) {
		super(null, null, color);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		float k = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
		if (Math.abs(k) > 1) {
			this.b = new Point(p3.getX() + 100, p3.getY() - 100 / k);
		} else {
			this.b = new Point(p3.getX() - 100 * k, p3.getY() + 100);
		}
		this.a = p3;
	}
	
	@Override
	public void update() {
		float k = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
		if (Math.abs(k) > 1) {
			b.moveTo(p3.getX() + 100, p3.getY() - 100 / k);
		} else {
			b.moveTo(p3.getX() - 100 * k, p3.getY() + 100);
		}
	}
	
	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> res = new ArrayList<>();
		res.add(p1);
		res.add(p2);
		res.add(p3);
		return res;
	}
}
