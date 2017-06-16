package org.trompgames.localmanga;

import java.util.ArrayList;

import org.trompgames.mangabase.MangaChapter;

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
