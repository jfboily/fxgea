package com.jfboily.fxgea;


import com.jfboily.fxgea.FxgeaSettings;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public abstract class Game extends Activity
{
	private Screen curScreen;
	private Audio audio;
	private Input input;
	private CanvasRenderer renderer;
	private LogicUpdater logic;
	
	private static Game instance;
	private float scaleX;
	private float scaleY;
	
	public static Game getGame()
	{
		return instance;
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        // lecture des settings
        FxgeaSettings.read(this);
        
        
        // lock l'orientation selon les settings
        if(FxgeaSettings.orientation == FxgeaSettings.Orientations.LANDSCAPE)
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        
        // Fullscreen! FTW!
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // no sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        instance = this;
        

        
        scaleX = (float)FxgeaSettings.resolutionX / (float)getWindowManager().getDefaultDisplay().getWidth();
        scaleY = (float)FxgeaSettings.resolutionY / (float)getWindowManager().getDefaultDisplay().getHeight();
        
        // instanciation des composantes
        boolean autoScale = FxgeaSettings.autoScale;
        // creation du renderer
        renderer = new CanvasRenderer(this, autoScale);
        
        audio = new Audio(this);
        input = new Input(this, renderer, autoScale);

        logic = new LogicUpdater(this);
        // creation de l'ecran
        curScreen = getStartScreen();
        
        // boom!
        setContentView(renderer);		
	}
	
	public abstract Screen getStartScreen();


	public Screen getCurrentScreen()
	{
		return curScreen;
	}

	public void setScreen(Screen newScreen)
	{
		curScreen.dispose();
		curScreen = null;
		curScreen = newScreen;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		audio.pause();
		logic.pause();
		renderer.pause();
		
		if(isFinishing())
		{
			//curScreen.destroy();
			curScreen.dispose();
			audio.dispose();
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		audio.resume();
		logic.resume();
		renderer.resume();
	}
	
	public Audio getAudio()
	{
		return audio;
	}
	
	public Input getInput()
	{
		return input;
	}
	
	public int getWidth()
	{
		return FxgeaSettings.resolutionX;
	}
	
	public int getHeight()
	{
		return FxgeaSettings.resolutionY;
	}
	
	public float getScaleX()
	{
		return scaleX;
	}
	
	public float getScaleY()
	{
		return scaleY;
	}
	
	public void quitGame()
	{
		finish();
	}
	
	public long getTime()
	{
		return renderer.getCurrentTime();
	}

}
