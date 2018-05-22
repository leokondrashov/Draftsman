package com.lk.draftsman.core;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawEngine implements Serializable {
	private Point p;
	private Shape shape;
	private final ArrayList<Point> points = new ArrayList<>();
	private final ArrayList<Shape> list = new ArrayList<>();
	private Tool tool = Tool.Drag;
	private int color = Color.BLACK;
	private boolean isLandscape;
	private int width;
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public enum Tool {
		Drag,
		Point,
		Line,
		Circle,
		PerpLine,
		CircleBy3P,
		ParalLine,
		MidPoint,
		Bisector
	}
	
	public DrawEngine(Context context) {
		isLandscape = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		width = (isLandscape ? context.getResources().getDisplayMetrics().heightPixels : context.getResources().getDisplayMetrics().widthPixels);
		Shape.paint.setColor(color);
		Shape.paint.setStyle(Paint.Style.STROKE);
		Shape.paint.setStrokeWidth(5);
	}
	
	public void checkOrientation(boolean isLandscape) {
		if (isLandscape ^ this.isLandscape) {
			if (!this.isLandscape) {
				synchronized (list) {
					for (Shape s : list) {
						if (s instanceof Point && !(s instanceof MidPoint) && !(s instanceof CenterPoint)) {
							((Point) s).moveTo(((Point) s).getY(), width - ((Point) s).getX());
						} else {
							s.update();
						}
					}
				}
				synchronized (points) {
					for (Point p : points) {
						p.moveTo(p.getY(), width - p.getX());
					}
				}
			} else {
				synchronized (list) {
					for (Shape s : list) {
						if (s instanceof Point && !(s instanceof MidPoint) && !(s instanceof CenterPoint)) {
							((Point) s).moveTo(width - ((Point) s).getY(), ((Point) s).getX());
						} else {
							s.update();
						}
					}
				}
				synchronized (points) {
					for (Point p : points) {
						p.moveTo(width - p.getY(), p.getX());
					}
				}
			}
			this.isLandscape = isLandscape;
		}
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
		Point tmp;
		if (tool == Tool.Drag) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					p = new Point(x, y);
					tmp = findNearestPoint(p);
					if (tmp != null) {
						p = tmp;
						p.setDependencies(null);
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
						p.moveTo(x, y);
						tmp = findNearest(p);
						if (tmp != null) {
							p.moveTo(tmp);
							if (tmp.getDependencies() != null && !tmp.getDependencies().isEmpty())
								p.setDependencies(tmp.getDependencies());
							else
								p.setDependencies(new ArrayList<Shape>(Collections.singletonList(tmp)));
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
					p = new Point(x, y, color);
					switch (tool) {
						case Line:
							if (points.size() == 1) {
								shape = new Line(points.get(0), p, color);
							}
							break;
						case Circle:
							if (points.size() == 1) {
								shape = new Circle(points.get(0), p, color);
							}
							break;
						case PerpLine:
							if (points.size() == 2) {
								shape = new PerpLine(points.get(0), points.get(1), p, color);
							}
							break;
						case CircleBy3P:
							if (points.size() == 2) {
								shape = new CircleBy3P(points.get(0), points.get(1), p, color);
							}
							break;
						case ParalLine:
							if (points.size() == 2) {
								shape = new ParalLine(points.get(0), points.get(1), p, color);
							}
							break;
						case MidPoint:
							if (points.size() == 1) {
								shape = new MidPoint(points.get(0), p, color);
							}
							break;
						case Bisector:
							if (points.size() == 2) {
								shape = new Bisector(points.get(0), points.get(1), p, color);
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
							if (!list.contains(tmp))
								tmp.setColor(color);
							points.add(tmp);
							if (!list.contains(tmp)) {
								synchronized (list) {
									list.add(tmp);
								}
							}
						} else {
							points.add(p);
							synchronized (list) {
								list.add(p);
							}
						}
					}
					p = null;
					if (shape != null) {
						switch (tool) {
							case Line:
								shape = new Line(points.get(0), points.get(1), color);
								break;
							case Circle:
								shape = new Circle(points.get(0), points.get(1), color);
								break;
							case PerpLine:
								shape = new PerpLine(points.get(0), points.get(1), points.get(2), color);
								break;
							case CircleBy3P:
								shape = new CircleBy3P(points.get(0), points.get(1), points.get(2), color);
								synchronized (list) {
									list.add(shape.getGeneralPoints().get(0));
								}
								break;
							case ParalLine:
								shape = new ParalLine(points.get(0), points.get(1), points.get(2), color);
								break;
							case MidPoint:
								shape = new MidPoint(points.get(0), points.get(1), color);
								break;
							case Bisector:
								shape = new Bisector(points.get(0), points.get(1), points.get(2), color);
						}
						synchronized (list) {
							list.add(shape);
						}
						shape = null;
						synchronized (points) {
							points.clear();
						}
					} else if (tool == Tool.Point) {
						synchronized (points) {
							points.clear();
						}
					}
			}
		}
	}
	
	public void clear() {
		synchronized (list) {
			list.clear();
		}
		synchronized (points) {
			points.clear();
		}
		p = null;
		shape = null;
	}
	
	public void setTool(Tool tool) {
		this.tool = tool;
		while (!points.isEmpty()) {
			if (points.get(points.size() - 1) == list.get(list.size() - 1))
				list.remove(list.size() - 1);
			points.remove(points.size() - 1);
		}
	}

	public void Undo() {
//		Log.d("com.lk", "Undo: \nlist-" + list.size() + "\npoints-" + points.size() + "\nshape-"
//				+ (list.isEmpty() ? "none" : list.get(list.size() - 1).getClass().getName()) + "\n"
//				+ (list.isEmpty() || points.isEmpty() ? "none" : list.get(list.size() - 1) == points.get(points.size() - 1)));
		synchronized (list) {
			synchronized (points) {
				if (list.isEmpty())
					return;
				if (!points.isEmpty()) {
					if (points.get(points.size() - 1) == list.get(list.size() - 1))
						list.remove(list.size() - 1);
					points.remove(points.size() - 1);
				} else if (list.get(list.size() - 1) instanceof Point && !(list.get(list.size() - 1) instanceof MidPoint)) {
					list.remove(list.size() - 1);
				} else {
					points.clear();
					points.addAll(list.get(list.size() - 1).getPoints());
					if (list.get(list.size() - 1) instanceof CircleBy3P)
						list.remove(list.size() - 2);
					if (points.get(points.size() - 1) == list.get(list.size() - 2))
						list.remove(list.size() - 2);
					points.remove(points.size() - 1);
					
					if (list.get(list.size() - 1) instanceof PerpLine)
						tool = Tool.PerpLine;
					else if (list.get(list.size() - 1) instanceof ParalLine)
						tool = Tool.ParalLine;
					else if (list.get(list.size() - 1) instanceof Bisector)
						tool = Tool.Bisector;
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
		ArrayList<Shape> tmp = new ArrayList<>(list);
		tmp.remove(p);
		for (int i = 0; i < tmp.size(); i++) {
			boolean flag = true;
			if (!(tmp.get(i) instanceof Point) || (tmp.get(i) instanceof MidPoint) || (tmp.get(i) instanceof CenterPoint)) {
				for (Point point : tmp.get(i).getPoints()) {
					flag &= tmp.contains(point);
				}
			} else if (((Point) tmp.get(i)).getDependencies() != null) {
				for (Shape shape : ((Point) tmp.get(i)).getDependencies()) {
					flag &= tmp.contains(shape);
				}
			}
			if (!flag) {
				tmp.remove(i);
				i--;
			}
		}
		for (Shape point : tmp) {
			if ((point instanceof Point) && point != p && p.distance((Point) point) < (minDist - EPS)) {
				minDist = p.distance((Point) point);
				res = (Point) point;
			}
		}
		
		ArrayList<Shape> shapes = new ArrayList<>();
		for (Shape shape : tmp) {
			if (!(shape instanceof Point) && shape.isNear(p)) {
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
