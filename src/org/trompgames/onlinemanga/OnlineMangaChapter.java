package org.trompgames.onlinemanga;

import java.util.ArrayList;

import org.trompgames.mangaviewer.MangaChapter;

public class OnlineMangaChapter extends MangaChapter{

	private ArrayList<String> baseURLS;
	private String baseURL;
	private String details;
	private int chapter;

	public OnlineMangaChapter(String name, String details, int chapter, String baseURL, ArrayList<String> baseURLS, int totalChapters){
		super(name, totalChapters);
		this.details = details;
		this.chapter = chapter;
		this.baseURL = baseURL;
		this.baseURLS = baseURLS;
	}

	public ArrayList<String> getBaseURLS() {
		return baseURLS;
	}
	
	public String getBaseURL(){
		return baseURL;
	}
	
	public void setBaseURLS(ArrayList<String> baseURLS){
		this.baseURLS = baseURLS;
	}
	
	public String getDetails(){
		return details;
	}
	
	public int getChapter(){
		return chapter;
	}
	
}
