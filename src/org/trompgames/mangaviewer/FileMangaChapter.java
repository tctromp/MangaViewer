package org.trompgames.mangaviewer;

import java.util.ArrayList;

public class FileMangaChapter extends MangaChapter{

	private ArrayList<String> imageFileNames = new ArrayList<String>();

	public FileMangaChapter(String name, ArrayList<String> imageFileNames){
		super(name, imageFileNames.size());
		this.imageFileNames = imageFileNames;
	}
	
	public ArrayList<String> getImageFileNames() {
		return imageFileNames;
	}
	
}
