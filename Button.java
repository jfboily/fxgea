package com.jfboily.fxgea;

import android.graphics.Rect;
import android.util.Log;


public class Button implements Touchable
{
	private Rect rect = new Rect();
	private ButtonClickListener listener = null;
	private Sprite sprite;
	private boolean active;
	
	public Button(int x, int y, String fname, Screen screen)
	{
		try
		{
			//InputStream is = Game.getGame().getAssets().open(fname);
			//bitmap = BitmapFactory.decodeStream(is);
			
			sprite = screen.createSprite(fname, Screen.PLANE_0);
			sprite.setPos(x, y);
			rect.left = x;
			rect.top = y;
			rect.right = x + sprite.getWidth();
			rect.bottom = y + sprite.getHeight();
			Game.getGame().getInput().registerTouchable(this);
			active = true;
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	public void setButtonClickListener(ButtonClickListener obj)
	{
		listener = obj;
	}

	public void onTouch(int x, int y) {
		// TODO Auto-generated method stub
		if(listener == null)
		{
			Log.e("Fxgea:Button", "Button.onTouch called with no listener defined.");
			throw new IllegalArgumentException("No ButtonClickListener defined for button!!!");
		}
		
		if(active && sprite.isVisible())
			listener.buttonClick(this);
	}

	public void onMove(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void onRelease(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public Rect getRect() {
		// TODO Auto-generated method stub
		return rect;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public void setVisible(boolean visible)
	{
		sprite.setVisible(visible);
	}
}
