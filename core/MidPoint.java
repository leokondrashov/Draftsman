package com.lk.draftsman.core;

import java.util.ArrayList;

public class MidPoint extends Point {
	
	private Point a, b;
	
	MidPoint(Point a, Point b) {
		super((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void update() {
		this.moveTo((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
	}
	
	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> res = new ArrayList<>();
		res.add(a);
		res.add(b);
		return res;
	}
}
