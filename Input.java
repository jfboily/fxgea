package com.jfboily.fxgea;

import java.util.ArrayList;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Input implements OnTouchListener
{
	// les touchables
	private ArrayList<Touchable> touchables;
	private Touchable selectedTouchable = null;	
	private float scaleX, scaleY;
	private float dx, dy;
	private boolean touchDown = false;
	private boolean touchMove = false;
	private boolean touchRelease = false;
	private int touchX, touchY;
	private int screenW, screenH;
	private boolean newTouchDown;
	
	public Input(Game game, CanvasRenderer renderer, boolean autoScale)
	{
		renderer.setOnTouchListener(this);
		touchables = new ArrayList<Touchable>(100);
		if(autoScale)
		{
			this.scaleX = game.getScaleX();
			this.scaleY = game.getScaleY();
			dx = 0.0f;
			dy = 0.0f;
		}
		else
		{
			this.scaleX = 1.0f;
			this.scaleY = 1.0f;
			//dx = (game.getWidth() * game.getScaleX() / 2.0f) - (game.getWidth() / 2.0f);
			//dy = (game.getHeight() * game.getScaleY() / 2.0f) - (game.getHeight() / 2.0f);
			dx = renderer.getScreenDX();
			dy = renderer.getScreenDY();
		}
		
		screenW = game.getWidth();
		screenH = game.getHeight();
	}
	
	public static class Touch
	{
		public static boolean isDown = false;
		public static boolean newDown = false;
		public static int x = 0;
		public static int y = 0;
		public static int dx = 0;
		public static int dy = 0;		
	}

	public boolean onTouch(View v, MotionEvent event) 
	{
		int x, y;
//		x = (int)(((event.getX() - dstRect.left) / (float)deviceW) * virtualW);
//		y = (int)(((event.getY() - dstRect.top) / (float)deviceH) * virtualH);
		
		x = (int)((event.getX() * scaleX) - dx);
		y = (int)((event.getY() * scaleY) - dy);

		touchX = x;
		touchY = y;
		
		if(touchX > screenW)
		{
			touchX = screenW - 1;
		}
		
		if(touchX < 0)
		{
			touchX = 0;
		}
		
		if(touchY > screenH)
		{
			touchY = screenH;
		}
		
		if(touchY < 0 )
		{
			touchY = 0;
		}
			
			
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			touchDown = true;	
			newTouchDown = true;
			break;
			
		case MotionEvent.ACTION_UP:
			touchRelease = true;
			break;
			
		case MotionEvent.ACTION_MOVE:			
			touchMove = true;
			break;
		}
		
		return true;
	}	
	
	public void registerTouchable(Touchable obj)
	{
		if(!touchables.contains(obj))
		{
			touchables.add(obj);
		}
	}
	
	public void unregisterTouchable(Touchable obj)
	{
		touchables.remove(obj);
	}
	
	public void unregisterAllTouchables()
	{
		touchables.clear();
	}
	
	public void update()
	{
		Touch.dx = touchX - Touch.x;
		Touch.dy = touchY - Touch.y;
		Touch.y = touchY;
		Touch.x = touchX;
		Touch.newDown = false;
		if(!Touch.isDown && touchDown)
		{
			Touch.newDown = true;
		}
		Touch.isDown = touchDown;
		
		if(newTouchDown)
		{
			for(int i = touchables.size()-1; i >= 0; i--)
			{
				Touchable t = touchables.get(i);
				Rect r = t.getRect();
				
				if(r.contains(touchX, touchY))
				{
					t.onTouch(touchX, touchY);
					selectedTouchable = t;
					break;
				}
			}
		}
		
		if(touchMove)
		{
			if(selectedTouchable != null)
			{
				selectedTouchable.onMove(touchX, touchY);
			}
		}
		
		if(touchRelease)
		{
			if(selectedTouchable != null)
			{
				selectedTouchable.onRelease(touchX, touchY);
				selectedTouchable = null;
			}
			
			touchDown = false;
			touchMove = false;
			touchRelease = false;
		}
		
		newTouchDown = false;
	}
}
