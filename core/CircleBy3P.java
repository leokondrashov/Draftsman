package com.lk.draftsman.core;

import java.util.ArrayList;

public class CircleBy3P extends Circle {
	
	private Point p1, p2, p3;
	
	CircleBy3P(Point p1, Point p2, Point p3) {
		super(new CenterPoint(p1, p2, p3), p3);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> res = new ArrayList<>();
		res.add(p1);
		res.add(p2);
		res.add(p3);
		return res;
	}
	
	@Override
	public void update() {
		c.update();
	}
}
