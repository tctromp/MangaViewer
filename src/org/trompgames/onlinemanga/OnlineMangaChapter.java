package org.trompgames.onlinemanga;

import java.util.ArrayList;

import org.trompgames.mangaviewer.MangaChapter;

public class OnlineMangaChapter extends MangaChapter{

	private ArrayList<String> baseURLS = new ArrayList<String>();
	private String details;
	private int chapter;

	public OnlineMangaChapter(String name, String details, int chapter, ArrayList<String> baseURLS){
		super(name, baseURLS.size());
		this.details = details;
		this.chapter = chapter;
		this.baseURLS = baseURLS;
	}

	public ArrayList<String> getBaseURLS() {
		return baseURLS;
	}
	
	public String getDetails(){
		return details;
	}
	
	public int getChapter(){
		return chapter;
	}
	
}
