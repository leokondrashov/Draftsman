package com.lk.draftsman.core;

import java.util.ArrayList;

public class ParalLine extends Line {
	
	private Point p1, p2, p3;
	
	ParalLine(Point p1, Point p2, Point p3) {
		super(null, null);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.b = new Point(p2.getX() + p3.getX() - p1.getX(), p2.getY() + p3.getY() - p1.getY());
		this.a = p3;
	}
	
	@Override
	public void update() {
		b.moveTo(p2.getX() + p3.getX() - p1.getX(), p2.getY() + p3.getY() - p1.getY());
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
