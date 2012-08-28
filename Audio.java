package com.jfboily.fxgea;

import java.util.Collection;
import java.util.HashMap;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Audio
{
	private SoundPool pool;
	private MediaPlayer mp;
	private Game game;
	private float sfxVolume = 0.8f;
	private float musicVolume = 0.8f;
	
	private HashMap<Integer, Integer> soundIds = new HashMap<Integer, Integer>(50);
	
	
	public Audio(Game game)
	{
		this.game = game;
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		mp = new MediaPlayer();
	}
	
	public void loadSound(int resId)
	{
		int id = 0;
		id = pool.load(game, resId, 1);
		
		soundIds.put(resId, id);
	}
	
	public void stopSound(int soundID)
	{
		pool.stop(soundID);
	}
	
	public int playSound(int resId, boolean loop)
	{
		Integer soundId = soundIds.get(resId);
		if(soundId != null)
		{
			return pool.play(soundId, sfxVolume, sfxVolume, 1, loop?-1:0, 1.0f);
		}
		else
		{
			Log.e("Fxgea:Audio", "Invalid sound resource : "+resId+" Not loaded??");
			Log.d("Fxgea:Audio", "Autoloading...");
			loadSound(resId);
			soundId = soundIds.get(resId);
			return pool.play(soundId, sfxVolume, sfxVolume, 1, loop?-1:0, 1.0f);
		}
	}
	
	public void loadMusic(int resId)
	{
		mp.release();
		mp = MediaPlayer.create(game, resId);
		
		return;
	}
	
	public void playMusic()
	{
		try
		{
			mp.setVolume(musicVolume, musicVolume);
			mp.start();
			mp.setLooping(true);
		}
		catch(Exception e)
		{}
	}
	
	public void pauseMusic()
	{
		try
		{
			mp.pause();
		}
		catch(Exception e){}
	}
	
	public void resumeMusic()
	{
		try
		{
			mp.start();
		}
		catch(Exception e){}
	}
	
	public void stopMusic()
	{
		try
		{
			mp.stop();
		}
		catch(Exception e){}
	}
	
	public void pause()
	{
		try
		{
			mp.pause();
			Collection<Integer> col = soundIds.values();
			
			if(col != null)
			{
				Integer[] ids = col.toArray(new Integer[0]);
				for(int i = 0; i < ids.length; i++)
				{
					pool.pause(ids[i]);
				}
			}
		}
		catch(Exception e)
		{}
	}
	
	public void resume()
	{
		try
		{
			mp.start();
			Collection<Integer> col = soundIds.values();
			
			if(col != null)
			{
				Integer[] ids = col.toArray(new Integer[0]);
				for(int i = 0; i < ids.length; i++)
				{
					pool.resume(ids[i]);
				}
			}
		}
		catch(Exception e)
		{}
	}
	
	
	public void setSFXVolume(float vol)
	{
		if(vol > 1.0f)
			vol = 1.0f;
		
		if(vol < 0.0f)
			vol = 0.0f;
		
		sfxVolume = vol;
	}
	
	public void setMusicVolume(float vol)
	{
		if(vol > 1.0f)
			vol = 1.0f;
		
		if(vol < 0.0f)
			vol = 0.0f;
		
		musicVolume = vol;
		try
		{
			mp.setVolume(musicVolume, musicVolume);
		}
		catch(Exception e){}
	}
	
	public boolean isMusicPlaying()
	{
		return mp.isPlaying();
	}
	
	public void dispose()
	{
		pause();
		pool.release();
		mp.release();
	}
}
