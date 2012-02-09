package com.jfboily.fxgea;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class FxgeaSettings 
{
	public enum Orientations
	{
		PORTRAIT,
		LANDSCAPE
	}
	
	public static int resolutionX = 800;
	public static int resolutionY = 480;
	public static boolean autoScale = false;
	public static Orientations orientation = Orientations.LANDSCAPE; 
	
	public static void read(Game game)
	{
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = game.getAssets().open("fxgea.settings.xml");
			
			doc = db.parse(is);
			
			// recherche un tag "fxgeasettings"
			NodeList settingsTags = doc.getElementsByTagName("fxgeasettings");
			if(settingsTags.getLength() <= 0)
			{
				throw new IllegalArgumentException("Fxgea:Settings");
			}
			
			// prend seulement le premier, s'il y en a plusieurs
			Node settingsParent = settingsTags.item(0);
			
			// tags enfants
			NodeList settings = settingsParent.getChildNodes();
			for(int i = 0; i < settings.getLength(); i++)
			{
				Node n = settings.item(i);
			
				String tag = n.getNodeName();
				
				if(tag.equals("resolutionX"))
				{
					FxgeaSettings.resolutionX = Integer.parseInt(n.getFirstChild().getNodeValue());
				}
				
				if(tag.equals("resolutionY"))
				{
					FxgeaSettings.resolutionY = Integer.parseInt(n.getFirstChild().getNodeValue());
				}
				
				if(tag.equals("autoScale"))
				{
					FxgeaSettings.autoScale = Boolean.parseBoolean(n.getFirstChild().getNodeValue());
				}
				
				if(tag.equals("orientation"))
				{
					int o = Integer.parseInt(n.getFirstChild().getNodeValue());
					
					if(o == 1)
					{
						FxgeaSettings.orientation = Orientations.LANDSCAPE;
					}
					else
					{
						FxgeaSettings.orientation = Orientations.PORTRAIT;
					}
				}
			}
			
			
		}catch(Exception e)
		{
			Log.e("Fxgea:Settings", "Cannont read settings file (fxgea.settings.xml)");
			Log.e("Fxgea:Settings", "Using default values");
			
			// utilisera les valeurs par defaut
		}
	}
}
