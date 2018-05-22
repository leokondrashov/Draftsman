package com.lk.draftsman.core;

import java.util.ArrayList;

public class CenterPoint extends Point {
	
	private Point p1, p2, p3;
	
	CenterPoint(Point p1, Point p2, Point p3, int color) {
		super(0, 0, color);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		float bx = p2.getX() - p1.getX(), by = p2.getY() - p1.getY();
		float cx = p3.getX() - p1.getX(), cy = p3.getY() - p1.getY();
		float d = 2 * (bx * cy - by * cx);
		float ux = p1.getX() + (cy * (bx * bx + by * by) - by * (cx * cx + cy * cy)) / d;
		float uy = p1.getY() + (bx * (cx * cx + cy * cy) - cx * (bx * bx + by * by)) / d;
		this.moveTo(ux, uy);
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
		float bx = p2.getX() - p1.getX(), by = p2.getY() - p1.getY();
		float cx = p3.getX() - p1.getX(), cy = p3.getY() - p1.getY();
		float d = 2 * (bx * cy - by * cx);
		float ux = p1.getX() + (cy * (bx * bx + by * by) - by * (cx * cx + cy * cy)) / d;
		float uy = p1.getY() + (bx * (cx * cx + cy * cy) - cx * (bx * bx + by * by)) / d;
		this.moveTo(ux, uy);
	}
}
