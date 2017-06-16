package org.trompgames.onlinemanga;

import java.awt.image.BufferedImage;

import org.trompgames.mangabase.Manga;

public class MangaImage {

	
	private BufferedImage image;
	private int chapter;
	private int page;
	
	public MangaImage(BufferedImage image, int chapter, int page) {
		this.image = image;
		this.chapter = chapter;
		this.page = page;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getChapter() {
		return chapter;
	}

	public int getPage() {
		return page;
	}

	
	
	
}
