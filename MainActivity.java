package com.lk.draftsman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.lk.draftsman.core.DrawEngine;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private final int TOOLS_COUNT = 2;
	private DrawEngine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitiateToolsButtons();
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

	private ArrayList<ImageButton> InitiateToolsButtons(){
		ArrayList<ImageButton> buttons = new ArrayList<>();
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
		for(int i = 0; i < TOOLS_COUNT; i++){
			ImageButton button = new ImageButton(this);
			button.setImageResource(R.drawable.pic0);
		}
		return buttons;
	}

	@Override
	public void onBackPressed() {
		engine.Undo();
	}
}
