package com.lk.draftsman.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

public class DrawEngine {
	Point p;
	Shape shape;
	ArrayList<Shape> list = new ArrayList<>();
	Tool tool = Tool.Drag;

	enum Tool {
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
		for (Shape s : list) {
			s.draw(canvas);
		}
	}

	public void touch(MotionEvent event) {

	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

}
