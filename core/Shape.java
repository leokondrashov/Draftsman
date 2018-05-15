package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public abstract class Shape {
	
	static Paint paint = new Paint();
	int color = Color.BLACK;

	public abstract void draw(Canvas canvas);

	public abstract ArrayList<Point> getPoints();
	
	public abstract ArrayList<Point> getGeneralPoints();
	
	abstract boolean isNear(Point other);
	
	abstract Point nearest(Point p);
	
	public void update() {
	}
	
	static ArrayList<Point> intersections(Shape s1, Shape s2) {
		if (s1 instanceof Line && s2 instanceof Line) {
			Point a1 = s1.getGeneralPoints().get(0);
			Point b1 = s1.getGeneralPoints().get(1);
			Point a2 = s2.getGeneralPoints().get(0);
			Point b2 = s2.getGeneralPoints().get(1);
			float k1 = (a1.getY() - b1.getY()) / (a1.getX() - b1.getX());
			float k2 = (a2.getY() - b2.getY()) / (a2.getX() - b2.getX());
			float x = (k1 * a1.getX() - k2 * a2.getX() - a1.getY() + a2.getY()) / (k1 - k2);
			float y = (a1.getY() - b1.getY()) / (a1.getX() - b1.getX()) * (x - a1.getX()) + a1.getY();
			ArrayList<Point> res = new ArrayList<>();
			res.add(new Point(x, y));
			return res;
		} else if (s1 instanceof Circle && s2 instanceof Line) {
			final float EPS = 0.01f;
			Point a1 = s1.getGeneralPoints().get(0); // Circle
			Point b1 = s1.getGeneralPoints().get(1);
			Point a2 = s2.getGeneralPoints().get(0); // Line
			Point b2 = s2.getGeneralPoints().get(1);
			float r = (float) Math.sqrt((a1.getX() - b1.getX()) * (a1.getX() - b1.getX()) + (a1.getY() - b1.getY()) * (a1.getY() - b1.getY()));
			if (((Line) s2).distance(a1) > r + EPS) {
				return new ArrayList<>();
			} else if (Math.abs(r - ((Line) s2).distance(a1)) < EPS) {
				ArrayList<Point> res = new ArrayList<>();
				res.add(s2.nearest(((Circle) s1).c));
				return res;
			}
			Point p = s2.nearest(a1);
			float x0 = p.getX(), y0 = p.getY();
			float dist = p.distance(a1);
			float a = (float) Math.sqrt(r * r - dist * dist);
			float k = (a2.getY() - b2.getY()) / (a2.getX() - b2.getX());
			float cos = (float) Math.sqrt(k * k + 1);
			ArrayList<Point> res = new ArrayList<>();
			res.add(new Point(x0 + a / cos, y0 + k * a / cos));
			res.add(new Point(x0 - a / cos, y0 - k * a / cos));
			return res;
		} else if (s2 instanceof Circle && s1 instanceof Line) {
			final float EPS = 0.01f;
			Point a1 = s2.getGeneralPoints().get(0); // Circle
			Point b1 = s2.getGeneralPoints().get(1);
			Point a2 = s1.getGeneralPoints().get(0); // Line
			Point b2 = s1.getGeneralPoints().get(1);
			float r = (float) Math.sqrt((a1.getX() - b1.getX()) * (a1.getX() - b1.getX()) + (a1.getY() - b1.getY()) * (a1.getY() - b1.getY()));
			if (((Line) s1).distance(a1) > r + EPS) {
				return new ArrayList<>();
			} else if (Math.abs(r - ((Line) s1).distance(a1)) < EPS) {
				ArrayList<Point> res = new ArrayList<>();
				res.add(s1.nearest(((Circle) s2).c));
				return res;
			}
			Point p = s1.nearest(a1);
			float x0 = p.getX(), y0 = p.getY();
			float dist = p.distance(a1);
			float a = (float) Math.sqrt(r * r - dist * dist);
			float k = (a2.getY() - b2.getY()) / (a2.getX() - b2.getX());
			float cos = (float) Math.sqrt(k * k + 1);
			ArrayList<Point> res = new ArrayList<>();
			res.add(new Point(x0 + a / cos, y0 + k * a / cos));
			res.add(new Point(x0 - a / cos, y0 - k * a / cos));
			return res;
		} else if (s1 instanceof Circle && s2 instanceof Circle) {
			Point a1 = s1.getGeneralPoints().get(0);
			Point b1 = s1.getGeneralPoints().get(1);
			Point a2 = s2.getGeneralPoints().get(0);
			Point b2 = s2.getGeneralPoints().get(1);
			float r1 = (float) Math.sqrt((a1.getX() - b1.getX()) * (a1.getX() - b1.getX()) + (a1.getY() - b1.getY()) * (a1.getY() - b1.getY()));
			float r2 = (float) Math.sqrt((a2.getX() - b2.getX()) * (a2.getX() - b2.getX()) + (a2.getY() - b2.getY()) * (a2.getY() - b2.getY()));
			if (a1.distance(a2) == 0) {
				return new ArrayList<>();
			}
			float a1a2 = a1.distance(a2);
			float cos = (-r2 * r2 + r1 * r1 + a1a2 * a1a2) / 2 / r1 / a1a2;
			float h = r1 * (float) Math.sqrt(1 - cos * cos);
			float x0 = a1.getX() + (a2.getX() - a1.getX()) / a1a2 * cos * r1;
			float y0 = a1.getY() + (a2.getY() - a1.getY()) / a1a2 * cos * r1;
			float k = -(a2.getX() - a1.getX()) / (a2.getY() - a1.getY());
			cos = (float) Math.sqrt(k * k + 1);
			ArrayList<Point> res = new ArrayList<>();
			res.add(new Point(x0 + h / cos, y0 + k * h / cos));
			res.add(new Point(x0 - h / cos, y0 - k * h / cos));
//			Log.d("com.lk", String.format("Intersections: %s\n%s\n%s\n%s\n%f, %f\n%f, %f\n%f, %f", a1.toString(), b1.toString(),
//					a2.toString(), b2.toString(), x0 + h / cos, y0 + k * h / cos, x0 - h / cos, y0 - k * h / cos, x0, y0));
			return res;
		}
		return new ArrayList<>();
	}
	
	public void setColor(int color) {
		this.color = color;
	}
}
