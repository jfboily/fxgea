package com.jfboily.fxgea;

import android.graphics.Rect;
import android.util.Log;


public class Button extends Sprite implements Touchable
{
	private Rect rect = new Rect();
	private ButtonClickListener listener = null;
	//private Sprite sprite;
	private boolean active;
	
	public Button(int x, int y, int w, int h, String fname, Screen listener)
	{
		super(fname, w, h, Sprite.RefPixel.TOP_LEFT);
		try
		{
			setPos(x, y);
			Game.getGame().getInput().registerTouchable(this);
			active = true;
			setFrame(0);
			listener.addUISprite(this);
			this.listener = (ButtonClickListener)listener;
		}
		catch(Exception e)
		{
			Log.e("Fxgea:Button", "Exception in constructor : "+e.getMessage());
		}
		
	}

	public boolean onTouch(int x, int y) {
		// TODO Auto-generated method stub
		if(listener == null)
		{
			Log.e("Fxgea:Button", "Button.onTouch called with no listener defined.");
			throw new IllegalArgumentException("No ButtonClickListener defined for button!!!");
		}
		
		if(active && isVisible())
		{
			listener.buttonClick(this);
			return true;
		}
		
		return false;
	}

	public boolean onMove(int x, int y) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean onRelease(int x, int y) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setActive(boolean active)
	{
		this.active = active;
		if(active)
			setFrame(0);
		else
			setFrame(1);
	}
	
	public boolean isActive()
	{
		return active;
	}
}
