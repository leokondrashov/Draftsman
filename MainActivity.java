package com.lk.draftsman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.lk.draftsman.core.DrawEngine;

public class MainActivity extends AppCompatActivity {

	private final int TOOLS_COUNT = 2;
	private DrawEngine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		engine = ((DrawView) findViewById(R.id.drawView)).getEngine();
	}


	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.drag:
				engine.setTool(DrawEngine.Tool.Drag);
				break;
			case R.id.point:
				engine.setTool(DrawEngine.Tool.Point);
				break;
			case R.id.line:
				engine.setTool(DrawEngine.Tool.Line);
				break;
			case R.id.circle:
				engine.setTool(DrawEngine.Tool.Circle);
				break;
		}
	}

	@Override
	public void onBackPressed() {
		engine.Undo();
	}
}
