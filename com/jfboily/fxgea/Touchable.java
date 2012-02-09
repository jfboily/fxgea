package com.jfboily.fxgea;

import android.graphics.Rect;

public interface Touchable
{
	public abstract void onTouch(int x, int y);
	public abstract void onMove(int x, int y);
	public abstract void onRelease(int x, int y);
	public abstract Rect getRect();
}
