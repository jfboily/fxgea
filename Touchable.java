package com.jfboily.fxgea;

import android.graphics.Rect;

public interface Touchable
{
	public abstract boolean onTouch(int x, int y);
	public abstract boolean onMove(int x, int y);
	public abstract boolean onRelease(int x, int y);
	public abstract Rect getRect();
}
