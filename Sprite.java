package com.jfboily.fxgea;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Sprite
{
	private Bitmap bitmap;
	private float x, y;
	private int frameW, frameH;
	private int nbFrames;
	private Rect[] frames;
	private int curFrame;
	private boolean animRunning;
	
	private float vx, vy;
	
	private ArrayList<int[]> anims;
	private int[] curAnim;
	private int animDelay = 16;
	private long animTime;
	private boolean animLoop = false;;
	private int animFrame = 0;
	
	private static HashMap<String, Bitmap> bitmaps = new HashMap<String, Bitmap>(100);
	
	public static final int DEFAULT_ANIM = 0;
	
	private boolean visible = true;
	private Rect dstRect = new Rect();
	
	private int refX = 0;
	private int refY = 0;
	
	private float angle = 0.0f;
	private int alpha = 255;;
	private Paint paint = new Paint();
	
	private Game game;
	
	public enum RefPixel
	{
		TOP_LEFT,
		TOP_RIGHT,
		TOP_CENTER,
		CENTER_LEFT,
		CENTER,
		CENTER_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT
	}
	
	private static Bitmap loadBitmap(String fname, Context context)
	{
		Bitmap bitmap = null;
		
		if(!bitmaps.containsKey(fname))
		{
			try
			{
				InputStream is = context.getAssets().open(fname);
				bitmap = BitmapFactory.decodeStream(is);
				bitmaps.put(fname, bitmap);
			}
			catch(IOException e)
			{
				
			}
		}
		else
		{
			bitmap = bitmaps.get(fname);
		}
		
		return bitmap;
	}
	
	public Sprite(String fname)
	{
		bitmap = loadBitmap(fname, Game.getGame());
		init(bitmap.getWidth(), bitmap.getHeight(), RefPixel.TOP_LEFT);
	}
	
	public Sprite(String fname, RefPixel refPixel)
	{
		bitmap = loadBitmap(fname, Game.getGame());
		init(bitmap.getWidth(), bitmap.getHeight(), refPixel);
	}
	
	public Sprite(String fname, int w, int h)
	{
		bitmap = loadBitmap(fname, Game.getGame());
		init(w, h, RefPixel.TOP_LEFT);
	}
	
	public Sprite(String fname, int w, int h, RefPixel refPixel)
	{
		bitmap = loadBitmap(fname, Game.getGame());
		init(w, h, refPixel);
	}
	
	
	private void init(int w, int h, RefPixel refPixel)
	{
		game = Game.getGame();
		x = 0;
		y = 0;
		frameW = w;
		frameH = h;
		
		int nbframesW = bitmap.getWidth() / w;
		int nbframesH = bitmap.getHeight() / h;
		nbFrames = nbframesW * nbframesH;
		frames = new Rect[nbFrames];
		int findex = 0;
		for(int j = 0; j < nbframesH; j++)
		{
			for(int i = 0; i < nbframesW; i++)
			{
				frames[findex] = new Rect(i * w, j * h, (i * w) + w, (j * h) + h);
				findex++;
			}
		}
		
		curFrame = 0;
		animRunning = false;
		anims = new ArrayList<int[]>(10);
		animDelay = 16;
		animTime = game.getTime();
		int[] defaultAnim = new int[nbFrames];
		for(int i = 0; i < nbFrames; i++)
		{
			defaultAnim[i] = i;
		}
		anims.add(defaultAnim);
		setRefPixel(refPixel);
	}
	
	
	public void draw(Canvas c)
	{
		long time = game.getTime();
		
		// ne dessine pas si invisible!
		if(!visible)
			return;
		
		if(animRunning)
		{
			setFrame(curAnim[animFrame]);
			
			if(time > animTime)
			{
				animTime = time + animDelay;
				animFrame++;
				if(animFrame >= curAnim.length)
				{
					if(animLoop)
					{
						animFrame = 0;
					}
					else
					{
						animRunning = false;
						curAnim = null;
					}
				}
			}
		}

		//avec rotation!!!
		c.save();
		c.rotate(angle, x, y);
		//c.drawBitmap(bitmap, x, y, null);
		// avec alpha!!
		paint.setAlpha(alpha);
		c.drawBitmap(bitmap, frames[curFrame], dstRect, paint);
		c.restore();
	}
	
	public void setRefPixel(RefPixel refPixel)
	{
		switch(refPixel)
		{
		case TOP_LEFT:
			refX = 0;
			refY = 0;
			break;
		case TOP_CENTER:
			refX = frameW / 2;
			refY = 0;
			break;
		case TOP_RIGHT:
			refX = frameW - 1;
			refY = 0;
			break;
		case CENTER_LEFT:
			refX = 0;
			refY = frameH / 2;
			break;
		case CENTER:
			refX = frameW / 2;
			refY = frameH / 2;
			break;
		case CENTER_RIGHT:
			refX = frameW - 1;
			refY = frameH / 2;
			break;
		case BOTTOM_LEFT:
			refX = 0;
			refY = frameH - 1;
			break;
		case BOTTOM_CENTER:
			refX = frameW / 2;
			refY = frameH - 1;
			break;
		case BOTTOM_RIGHT:
			refX = frameW - 1;
			refY = frameH - 1;
			break;			
		}
		
		updateDstRect();
	}
	
	private void updateDstRect()
	{
		dstRect.left = (int)x - refX;
		dstRect.top = (int)y - refY;
		dstRect.right = dstRect.left + frameW;
		dstRect.bottom = dstRect.top + frameH;
	}
	
	public void setRefPixel(int refX, int refY)
	{
		this.refX = refX;
		this.refY = refY;
		updateDstRect();
	}
	
	public void setPos(int x, int y)
	{
		this.x = x;
		this.y = y;
		updateDstRect();
	}
	
	public void move(int dx, int dy)
	{
		x += dx;
		y += dy;
		updateDstRect();
	}
	
	public void move()
	{
		x += vx;
		y += vy;
		updateDstRect();
	}
	
	public void setSpeed(float vx, float vy)
	{
		this.vx = vx;
		this.vy = vy;
	}
	
	public int getX()
	{
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	public int getWidth()
	{
		return frameW;
	}
	
	public int getHeight()
	{
		return frameH;
	}
	
	public boolean isAnimDone()
	{
		return !animRunning;
	}

	public Rect getRect()
	{
		return dstRect;
	}
	
	
	public void setFrame(int frame)
	{
		if(frame >= 0 && frame < nbFrames)
		{
			curFrame = frame;
		}
	}
	
	public int createAnim(int[] anim)
	{
		if(anims != null)
		{
			anims.add(anim);
			return anims.size() - 1;
		}
		
		Log.e("FXGEA.Sprite", "Cannot create anim on single frame sprite");
		return -1;
	}
	
	public void playAnim(int animId, boolean loop)
	{
		int[] anim =anims.get(animId); 
		if(anim == null)
		{
			Log.e("FXGEA.Sprite", "Invalid animId");
			return;
		}
				
		animRunning = true;
		curAnim = anim;
		animFrame = 0;
		animLoop = loop;		
	}
	
	public void setAnimDelay(int ms)
	{
		animDelay = ms;
		animTime = game.getTime() + animDelay;
	}
	
	public void stopAnim()
	{
		if(curAnim != null)
		{
			curFrame = curAnim[0];
		}
		
		animRunning = false;
	}
	
	public void pauseAnim()
	{
		animRunning = false;
	}
	
	public void resumeAnim()
	{
		if(curAnim != null)
		{
			animRunning = true;
			animTime = game.getTime() + animDelay;
		}
	}	
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public void rotate(float angle)
	{
		this.angle = angle;
	}
	
	public void rotateTo(int x, int y)
	{
		float delta = -90.0f;

		angle = (float)(Math.atan2(this.y - y, this.x - x) / Math.PI * 180) + delta;
	}
	
	public boolean collidesWith(Sprite s2)
	{
		return Rect.intersects(this.dstRect, s2.dstRect);
	}
	
	public void setAlpha(int alpha)
	{
		if(alpha > 255)
			alpha = 255;
		
		if(alpha < 0)
			alpha = 0;
		
		this.alpha = alpha;
	}
	
	public int getAlpha()
	{
		return alpha;
	}
}
