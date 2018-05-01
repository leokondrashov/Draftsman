package com.lk.draftsman.core;

import java.util.ArrayList;

public class Bisector extends Line {
	private Point p1, p2, p3;
	
	public Bisector(Point p1, Point p2, Point p3) {
		super(p2, null);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		float d1 = p2.distance(p1);
		float d2 = p2.distance(p3);
		float x = (d2 * p1.getX() + d1 * p3.getX()) / (d1 + d2);
		float y = (d2 * p1.getY() + d1 * p3.getY()) / (d1 + d2);
		b = new Point(x, y);
	}
	
	@Override
	public void update() {
		float d1 = p2.distance(p1);
		float d2 = p2.distance(p3);
		float x = (d2 * p1.getX() + d1 * p3.getX()) / (d1 + d2);
		float y = (d2 * p1.getY() + d1 * p3.getY()) / (d1 + d2);
		b.moveTo(x, y);
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
