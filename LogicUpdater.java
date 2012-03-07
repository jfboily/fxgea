package com.jfboily.fxgea;

import android.util.Log;

public class LogicUpdater implements Runnable
{
	private Thread logicThread;
	private boolean running = false;
	private Screen screen;
	private Input input;
	private Game game;
	private long startTime, endTime, deltaTime;
	
	public LogicUpdater(Game game)
	{
		this.game = game;
	}

	public void run() 
	{
		startTime = System.currentTimeMillis();
		endTime = startTime;
		deltaTime = 1;
		while(running)
		{
			startTime = System.currentTimeMillis();
			input = game.getInput();
			screen = game.getCurrentScreen();
			
			// update inputs
			input.update();
			
			// update the screen
			screen.superUpdate(startTime, deltaTime);
			try
			{
				Thread.sleep(2);
			}catch(Exception e){}
			endTime = System.currentTimeMillis();
			deltaTime = endTime - startTime;
		}
	}
	
	public void pause()
	{
		boolean ok = false;
		
		// est-ce que le thread existe??
		if(logicThread == null)
			return;
		
		// stoppe le thread en quittant la boucle
		running = false;
		
		// attend la fin du rendu de dernier frame
		while(!ok)
		{
			try
			{
				logicThread.join();
				ok = true;
			}
			catch(Exception e)
			{}
		}
	}
	
	public void resume()
	{
		running = true;
		logicThread = new Thread(this);
		logicThread.start();
	}

}
