package com.lk.draftsman.core;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Point extends Shape {
	private float x, y;
	
	private ArrayList<Shape> dependencies = null;
	
	Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(x, y, 5, isHighlight ? highlight : notHighlight);
	}

	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(this);
		return tmp;
	}
	
	@Override
	public ArrayList<Point> getGeneralPoints() {
		ArrayList<Point> tmp = new ArrayList<>();
		tmp.add(this);
		return tmp;
	}
	
	void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	void moveTo(Point oth) {
		if (oth == null)
			return;
		this.x = oth.x;
		this.y = oth.y;
	}
	
	float getX() {
		return x;
	}
	
	float getY() {
		return y;
	}
	
	boolean isNear(Point other) {
		return distance(other) < 50;
	}
	
	@Override
	Point nearest(Point p) {
		return this;
	}
	
	float distance(Point other) {
		return (float) Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
	}
	
	void setDependencies(ArrayList<Shape> dependencies) {
		this.dependencies = dependencies;
	}
	
	ArrayList<Shape> getDependencies() {
		return dependencies;
	}
	
	@Override
	public void update() {
		if (dependencies == null || dependencies.isEmpty()) {
		} else if (dependencies.size() == 1) {
			moveTo(dependencies.get(0).nearest(this));
		} else {
			ArrayList<Point> points = Shape.intersections(dependencies.get(0), dependencies.get(1));
			if (points.isEmpty()) {
			} else if (points.size() == 1) {
				moveTo(points.get(0));
			} else if (distance(points.get(0)) < distance(points.get(1))) {
				moveTo(points.get(0));
			} else {
				moveTo(points.get(1));
			}
		}
	}
	
	@Override
	public String toString() {
		return "Point(" + x + ", " + y + ")";
	}
}
