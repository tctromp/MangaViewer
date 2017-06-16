package org.trompgames.onlinemanga;

import org.trompgames.mangabase.Manga;

public class OnlineManga extends Manga{

	private MangaHereManga manga;
	
	public OnlineManga(String name, MangaHereManga manga) {
		super(name);
		this.manga = manga;
	}

	@Override
	public int getPages(int chapter) {
		return manga.getPages(chapter);
	}

	@Override
	public int getTotalChapters() {
		return manga.getTotalChapters();
	}
	
	public String getChapterName(int chapter){
		return manga.getOnlineMangaChapters().get(chapter).getName();
	}

}
