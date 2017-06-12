package org.trompgames.mangaviewer;

import java.util.ArrayList;

public class Manga {

	
	private String name;
	private ArrayList<MangaChapter> chapters = new ArrayList<MangaChapter>();
	private int totalPages = 0;	
	
	public Manga(String name, ArrayList<MangaChapter> chapters){
		this.name = name;
		this.chapters = chapters;
		loadTotalPages();
	}

	private void loadTotalPages(){
		totalPages = 0;
		for(MangaChapter chapter : chapters){
			totalPages += chapter.getImageFileNames().size();
		}		
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<MangaChapter> getChapters() {
		return chapters;
	}
	
	public int getTotalPages(){
		return totalPages;
	}
	
	public int getCumulativePages(int page, int chapter){
		int pages = 0;
		
		for(int i = 0; i < chapters.size(); i++){
			MangaChapter c = chapters.get(i);
			if(i == chapter){			
				pages += page + 1;
				break;
			}
			pages += c.getPages();
		}
		return pages;
		
	}
	
	
}
