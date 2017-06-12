package org.trompgames.mangaviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class MangaProperties {

	private File file = new File("mangaData");

	//private Properties properties;
	
	private MangaViewerHandler handler;
	private Manga manga;
	
	private int chapter = 0;
	private int page = 0;
	
	
	
	public MangaProperties(MangaViewerHandler handler, Manga manga){
		this.handler = handler;
		this.manga = manga;
		loadProperties();		
	}
	
	public Properties loadProperties(){		
		
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			fileInputStream.close();
			
			
			Enumeration e = properties.keys();
			while(e.hasMoreElements()){				
				String key = (String) e.nextElement();
				String value = properties.getProperty(key);				

				if(key.equals("manga." + manga.getName() + ".chapter")){
					
					this.chapter = Integer.parseInt(value);
					
				}else if(key.equals("manga." + manga.getName() + ".page")){
					
					this.page = Integer.parseInt(value);
					
				}
				
			}
			
			return properties;
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public void saveProperties(){
		
		
		Properties properties = loadProperties();
		
		properties.setProperty("manga." + manga.getName() + ".chapter", "" + handler.getCurrentChapter());
		properties.setProperty("manga." + manga.getName() + ".page", "" + handler.getCurrentPage());
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			properties.store(out, "Manga Data");			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	

	public int getChapter() {
		return chapter;
	}

	public int getPage() {
		return page;
	}
	
}
