package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawEngine {
	private Point p, tmp = null;
	private Shape shape;
	private ArrayList<Point> points = new ArrayList<>();
	private ArrayList<Shape> list = new ArrayList<>();
	private Tool tool = Tool.Drag;
	
	public enum Tool {
		Drag,
		Point,
		Line,
		Circle,
		PerpLine,
		CircleBy3P,
		ParalLine,
		MidPoint
	}

	public DrawEngine() {
		Shape.highlight.setColor(Color.BLUE);
		Shape.highlight.setStyle(Paint.Style.STROKE);
		Shape.highlight.setStrokeWidth(5);
		Shape.notHighlight.setColor(Color.BLACK);
		Shape.notHighlight.setStyle(Paint.Style.STROKE);
		Shape.notHighlight.setStrokeWidth(5);
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		synchronized (list) {
			for (Shape s : list) {
				s.draw(canvas);
			}
		}
		if (p != null) p.draw(canvas);
		synchronized (points) {
			if (!points.isEmpty())
				for (Point point : points)
					point.draw(canvas);
		}
		if (shape != null) shape.draw(canvas);
	}

	public void touch(MotionEvent event) {
		float x = event.getX(), y = event.getY();
		if (tool == Tool.Drag) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					p = new Point(event.getX(), event.getY());
					tmp = findNearestPoint(p);
					if (tmp != null) {
						p = tmp;
						p.setHighlight(true);
						p.setDependencies(null);
						tmp = null;
					} else {
						p = null;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (p != null) {
						p.moveTo(x, y);
						p.moveTo(findNearest(p));
						for (Shape s : list) {
							if (s != p) {
								s.update();
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if (p != null) {
						p.setHighlight(false);
						p.moveTo(x, y);
						tmp = findNearest(p);
						if (tmp != null) {
							p.moveTo(tmp);
							if (tmp.getDependencies() != null && !tmp.getDependencies().isEmpty())
								p.setDependencies(tmp.getDependencies());
							else
								p.setDependencies(new ArrayList<Shape>(Collections.singletonList(tmp)));
							tmp = null;
						}
						for (Shape point : list) {
							if ((point instanceof Point) && point != p) {
								point.update();
							}
						}
						p = null;
					}
			}
		} else {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					p = new Point(x, y);
					p.setHighlight(true);
					switch (tool) {
						case Line:
							if (points.size() == 1) {
								shape = new Line(points.get(0), p);
								shape.setHighlight(true);
							}
							break;
						case Circle:
							if (points.size() == 1) {
								shape = new Circle(points.get(0), p);
								shape.setHighlight(true);
							}
							break;
						case PerpLine:
							if (points.size() == 2) {
								shape = new PerpLine(points.get(0), points.get(1), p);
								shape.setHighlight(true);
							}
							break;
						case CircleBy3P:
							if (points.size() == 2) {
								shape = new CircleBy3P(points.get(0), points.get(1), p);
								shape.setHighlight(true);
							}
							break;
						case ParalLine:
							if (points.size() == 2) {
								shape = new ParalLine(points.get(0), points.get(1), p);
								shape.setHighlight(true);
							}
							break;
						case MidPoint:
							if (points.size() == 1) {
								shape = new MidPoint(points.get(0), p);
								shape.setHighlight(true);
							}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					p.moveTo(x, y);
					p.moveTo(findNearest(p));
					if (shape != null)
						shape.update();
					break;
				case MotionEvent.ACTION_UP:
					p.moveTo(x, y);
					synchronized (points) {
						tmp = findNearest(p);
						if (tmp != null) {
							points.add(tmp);
							if (tmp != findNearestPoint(p)) {
								synchronized (list) {
									list.add(tmp);
								}
							}
							tmp.setHighlight(true);
							tmp = null;
						} else {
							points.add(p);
							synchronized (list) {
								list.add(p);
							}
						}
					}
					p = null;
					if (shape != null) {
						shape.setHighlight(false);
						switch (tool) {
							case Line:
								shape = new Line(points.get(0), points.get(1));
								break;
							case Circle:
								shape = new Circle(points.get(0), points.get(1));
								break;
							case PerpLine:
								shape = new PerpLine(points.get(0), points.get(1), points.get(2));
								break;
							case CircleBy3P:
								shape = new CircleBy3P(points.get(0), points.get(1), points.get(2));
								synchronized (list) {
									list.add(shape.getGeneralPoints().get(0));
								}
								break;
							case ParalLine:
								shape = new ParalLine(points.get(0), points.get(1), points.get(2));
								break;
							case MidPoint:
								shape = new MidPoint(points.get(0), points.get(1));
						}
						synchronized (list) {
							list.add(shape);
						}
						shape = null;
						synchronized (points) {
							for (Point point : points) {
								point.setHighlight(false);
							}
							points.clear();
						}
					} else if (tool == Tool.Point) {
						synchronized (points) {
							points.get(0).setHighlight(false);
							points.clear();
						}
					}
			}
		}
	}

	public void setTool(Tool tool) {
		this.tool = tool;
		for (Point point : points) {
			point.setHighlight(false);
		}
		points.clear();
	}

	public void Undo() {
		Log.d("com.lk", "Undo: \nlist-" + list.size() + "\npoints-" + points.size() + "\nshape-"
				+ (list.isEmpty() ? "none" : list.get(list.size() - 1).getClass().getName()) + "\n"
				+ (list.isEmpty() || points.isEmpty() ? "none" : list.get(list.size() - 1) == points.get(points.size() - 1)));
		synchronized (list) {
			synchronized (points) {
				if (list.isEmpty())
					return;
				if (!points.isEmpty()) {
					if (points.get(points.size() - 1) == list.get(list.size() - 1))
						list.remove(list.size() - 1);
					points.get(points.size() - 1).setHighlight(false);
					points.remove(points.size() - 1);
				} else if (list.get(list.size() - 1) instanceof Point && !(list.get(list.size() - 1) instanceof MidPoint)) {
					list.remove(list.size() - 1);
				} else {
					points = list.get(list.size() - 1).getPoints();
					if (list.get(list.size() - 1) instanceof CircleBy3P)
						list.remove(list.size() - 2);
					if (points.get(points.size() - 1) == list.get(list.size() - 2))
						list.remove(list.size() - 2);
					points.remove(points.size() - 1);
					for (Point point : points) {
						point.setHighlight(true);
					}
					
					if (list.get(list.size() - 1) instanceof PerpLine)
						tool = Tool.PerpLine;
					else if (list.get(list.size() - 1) instanceof ParalLine)
						tool = Tool.ParalLine;
					else if (list.get(list.size() - 1) instanceof CircleBy3P)
						tool = Tool.CircleBy3P;
					else if (list.get(list.size() - 1) instanceof Line)
						tool = Tool.Line;
					else if (list.get(list.size() - 1) instanceof Circle)
						tool = Tool.Circle;
					else if (list.get(list.size() - 1) instanceof MidPoint)
						tool = Tool.MidPoint;
					list.remove(list.size() - 1);
				}
			}
		}
	}
	
	private Point findNearestPoint(Point p) {
		Point res = null;
		float minDist = 50;
		for (Shape point : list) {
			if ((point instanceof Point) && !(point instanceof MidPoint) && !(point instanceof CenterPoint) && p.distance((Point) point) < minDist) {
				res = (Point) point;
				minDist = p.distance((Point) point);
			}
		}
		return res;
	}
	
	private Point findNearest(Point p) {
		Point res = null;
		float minDist = 50;
		float EPS = 0.01f;
		for (Shape point : list) {
			if ((point instanceof Point) && point != p && p.distance((Point) point) < (minDist - EPS)) {
				minDist = p.distance((Point) point);
				res = (Point) point;
			}
		}
		ArrayList<Shape> shapes = new ArrayList<>();
		for (Shape shape : list) {
			if (!(shape instanceof Point) && shape.isNear(p) && !shape.getPoints().contains(p)) {
				shapes.add(shape);
			}
		}
		
		if (res == null && shapes.size() == 1) {
			res = shapes.get(0).nearest(p);
			res.setDependencies(shapes);
			return res;
		}
		
		for (int i = 0; i < shapes.size(); i++) {
			for (int j = i + 1; j < shapes.size(); j++) {
				for (Point point : Shape.intersections(shapes.get(i), shapes.get(j))) {
					if (p.distance(point) < (minDist - EPS)) {
						minDist = p.distance(point);
						res = point;
						res.setDependencies(new ArrayList<>(Arrays.asList(shapes.get(i), shapes.get(j))));
					}
				}
			}
		}
		
		if (res == null) {
			for (Shape shape : shapes) {
				Point t = shape.nearest(p);
				if (t.distance(p) < (minDist - EPS)) {
					minDist = t.distance(p);
					res = t;
					res.setDependencies(new ArrayList<>(Collections.singletonList(shape)));
				}
			}
		}
		return res;
	}
}
