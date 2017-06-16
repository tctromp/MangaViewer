package org.trompgames.localmanga;

import java.util.ArrayList;

import org.trompgames.mangabase.Manga;
import org.trompgames.mangabase.MangaChapter;

public class FileManga extends Manga{

	private ArrayList<FileMangaChapter> chapters = new ArrayList<FileMangaChapter>();
	private int totalPages = 0;	
	
	public FileManga(String name, ArrayList<FileMangaChapter> chapters) {
		super(name);
		this.chapters = chapters;
		loadTotalPages();
	}

	private void loadTotalPages(){
		totalPages = 0;
		for(FileMangaChapter chapter : chapters){
			totalPages += chapter.getImageFileNames().size();
		}		
	}
	
	public ArrayList<FileMangaChapter> getChapters() {
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

	@Override
	public int getPages(int chapter) {
		return chapters.get(chapter).getPages();
	}

	@Override
	public int getTotalChapters() {
		return chapters.size();
	}
	
	
	
}
