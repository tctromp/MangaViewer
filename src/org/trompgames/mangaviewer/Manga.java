package org.trompgames.mangaviewer;

public abstract class Manga {

	private String name;
	
	
	public Manga(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
	
	
	public abstract int getPages(int chapter);

	public abstract int getTotalChapters();
	
}
