package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

public class DrawEngine {
	private Point p;
	private Shape shape;
	private ArrayList<Shape> list = new ArrayList<>();
	private Tool tool = Tool.Drag;
	private int count = 0;

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
		if (shape != null) shape.draw(canvas);
	}

	public void touch(MotionEvent event) {
		if (tool == Tool.Drag) {
			// TODO dragging
		} else {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					p = new Point(event.getX(), event.getY());
					p.setHighlight(true);
					count++;
					switch (tool) {
						case Line:
							if (count == 2) {
								shape = new Line((Point) list.get(list.size() - 1), p);
								shape.setHighlight(true);
							}
							break;
						case Circle:
							if (count == 2) {
								shape = new Circle((Point) list.get(list.size() - 1), p);
								shape.setHighlight(true);
							}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					p.moveTo(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					p.moveTo(event.getX(), event.getY());
					p.setHighlight(false);
					synchronized (list) {
						list.add(p);
					}
					p = null;
					if (shape != null) {
						shape.setHighlight(false);
						synchronized (list) {
							list.add(shape);
						}
						shape = null;
						count = 0;
					} else if (tool == Tool.Point) {
						count = 0;
					}
			}
		}
	}

	public void setTool(Tool tool) {
		this.tool = tool;
		for (; count > 0; count--)
			list.remove(list.size() - 1);
	}

	public void Undo() {
//		Log.d("com.lk", "Undo: " + list.get(list.size() - 1).getClass().getName());
		synchronized (list) {
			if (list.isEmpty())
				return;
			if (list.get(list.size() - 1).getClass().getName().equals("com.lk.draftsman.core.Point")) {
				list.remove(list.size() - 1);
				count = Math.max(0, count - 1);
			} else if (list.get(list.size() - 1).getClass().getName().equals("com.lk.draftsman.core.Line") ||
					list.get(list.size() - 1).getClass().getName().equals("com.lk.draftsman.core.Circle")) {
				list.remove(list.size() - 1);
				list.remove(list.size() - 1);
				count = 1;
			}
		}
	}

}
