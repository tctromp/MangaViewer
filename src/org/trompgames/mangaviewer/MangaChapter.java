package org.trompgames.mangaviewer;

import java.util.ArrayList;

public class MangaChapter {

	private String name;
	private int pages;	
	
	private ArrayList<String> imageFileNames = new ArrayList<String>();
	
	public MangaChapter(String name, ArrayList<String> imageFileNames){
		this.name = name;
		this.imageFileNames = imageFileNames;
		this.pages = imageFileNames.size();
	}

	public String getName() {
		return name;
	}

	public int getPages() {
		return pages;
	}

	public ArrayList<String> getImageFileNames() {
		return imageFileNames;
	}
	
	
}
