package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

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
		Circle
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
		if (!points.isEmpty())
			for (Point point : points)
				point.draw(canvas);
		if (shape != null) shape.draw(canvas);
	}

	public void touch(MotionEvent event) {
		float x = event.getX(), y = event.getY();
		//TODO setting points on other shapes
		if (tool == Tool.Drag) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					p = new Point(event.getX(), event.getY());
					tmp = findNearest(p);
					if (tmp != null) {
						p = tmp;
						p.setHighlight(true);
						tmp = null;
					} else {
						p = null;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (p != null) {
						p.moveTo(x, y);
						p.moveTo(findNearest(p));
						for (Shape point : list) {
							if ((point instanceof Point) && point != p) {
								((Point) point).update();
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if (p != null) {
						p.setHighlight(false);
						p.moveTo(x, y);
						p.moveTo(findNearest(p));
						for (Shape point : list) {
							if ((point instanceof Point) && point != p) {
								((Point) point).update();
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
					}
					break;
				case MotionEvent.ACTION_MOVE:
					p.moveTo(x, y);
					p.moveTo(findNearest(p));
					break;
				case MotionEvent.ACTION_UP:
					p.moveTo(x, y);
					p.setHighlight(false);
					synchronized (points) {
						tmp = findNearest(p);
						if (tmp != null) {
							points.add(tmp);
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
						}
						synchronized (list) {
							list.add(shape);
						}
						shape = null;
						for (Point point : points) {
							point.setHighlight(false);
						}
						points.clear();
					} else if (tool == Tool.Point) {
						points.get(0).setHighlight(false);
						points.clear();
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
				} else if (list.get(list.size() - 1) instanceof Point) {
					list.remove(list.size() - 1);
				} else if (list.get(list.size() - 1) instanceof Line) {
					points = list.get(list.size() - 1).getPoints();
					if (points.get(points.size() - 1) == list.get(list.size() - 2))
						list.remove(list.size() - 2);
					points.remove(points.size() - 1);
					for (Point point : points) {
						point.setHighlight(true);
					}
					tool = Tool.Line;
					list.remove(list.size() - 1);
				} else if (list.get(list.size() - 1) instanceof Circle) {
					points = list.get(list.size() - 1).getPoints();
					if (points.get(points.size() - 1) == list.get(list.size() - 2))
						list.remove(list.size() - 2);
					points.remove(points.size() - 1);
					for (Point point : points) {
						point.setHighlight(true);
					}
					tool = Tool.Circle;
					list.remove(list.size() - 1);
				}
			}
		}
	}
	
	public Point findNearest(Point p) {
		for (Shape point : list) {
			if ((point instanceof Point) && p.isNear((Point) point)) {
				return (Point) point;
			}
		}
		return null;
	}
}
