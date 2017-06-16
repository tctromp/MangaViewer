package org.trompgames.mangaviewer;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.trompgames.gui.MangaViewerFrame;
import org.trompgames.gui.MangaViewerPanel;
import org.trompgames.localmanga.FileManga;
import org.trompgames.localmanga.FileMangaChapter;
import org.trompgames.mangabase.Manga;
import org.trompgames.mangainfo.MangaFitType;
import org.trompgames.mangaloaing.MangaProperties;
import org.trompgames.onlinemanga.MangaHereManga;
import org.trompgames.onlinemanga.MangaImage;
import org.trompgames.onlinemanga.OnlineManga;

public class MangaViewerHandler {

	private MangaViewerFrame frame;
	private MangaViewerPanel panel;
	
	private int screenWidth;
	private int screenHeight;
	
	private MangaHereManga onlineManga;
	
	private File mangaFolder;
	private File currentMangaFile;
	
	private ZipFile zipFile;	
	
	private Manga currentManga;
	private int currentChapter = 0;
	private int currentPage = 0;
	private int yOffset = 0;
	private double scrollWidthMultiplier = 1;
	
	private int currentScroll = 0;
	
	public static int SCROLLMULTIPLIER = 5;
	public static double SCROLLRATIO = 0.5;
	
	private BufferedImage previousImage;
	private BufferedImage currentImage;
	private BufferedImage nextImage;
	
	
	private boolean updateImages = false;
	private boolean wasUpdateNext = true;
	
	MangaProperties properties;
	
	
	
	public MangaViewerHandler() {
		
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		screenWidth = (int) rect.getWidth();
		screenHeight = (int) rect.getHeight();
		
		mangaFolder = new File("F:/Users/Thomas/Media/Manga");		
		
		MangaImageLoaderThread imageLoader = new MangaImageLoaderThread(this);		
		imageLoader.start();
		
		MangaFrameUpdaterThread frameUpdater = new MangaFrameUpdaterThread(this);		
		frameUpdater.start();
		
		this.frame = new MangaViewerFrame(this, screenWidth, screenHeight);
		this.panel = frame.getMangaViewerPanel();		
		

		this.getMangaViewerFrame().updateTitle();

	}

	public void setOnlineManga(String url){
		onlineManga = new MangaHereManga(url);
		
		this.currentManga = onlineManga.getManga();
		properties = new MangaProperties(this, currentManga);

		this.currentChapter = properties.getChapter();
		this.currentPage = properties.getPage();
		
		this.currentScroll = 0;

		if(this.getMangaViewerFrame() != null)
			this.getMangaViewerFrame().updateTitle();
		reload();
	}
	
	public void setOnlineManga(MangaHereManga mangaHereManga){
		properties = new MangaProperties(this, mangaHereManga.getManga());
		setOnlineManga(mangaHereManga, properties.getChapter(), properties.getPage());
	}
	
	public void setOnlineManga(MangaHereManga mangaHereManga, int chapter){
		properties = new MangaProperties(this, mangaHereManga.getManga());
		int page = properties.getPage();
		if(properties.getChapter() != chapter) page = 0;
		setOnlineManga(mangaHereManga, chapter, page);
	}
	
	public void setOnlineManga(MangaHereManga mangaHereManga, int chapter, int page){
		onlineManga = mangaHereManga;
		
		this.currentManga = onlineManga.getManga();

		this.currentChapter = chapter;
		this.currentPage = page;
		
		this.currentScroll = 0;
		
		if(this.getMangaViewerFrame() != null)
			this.getMangaViewerFrame().updateTitle();
		reload();
		
	}
	
	public static class MangaFrameUpdaterThread extends Thread{
		
		private MangaViewerHandler handler;
		
		public MangaFrameUpdaterThread(MangaViewerHandler handler){
			this.handler = handler;
		}	
		
		@Override
		public void run(){
			long lastTime = System.currentTimeMillis();		
			
			boolean updating = false;
			while(true){				
				if(!(System.currentTimeMillis() > lastTime + 17))continue;			
					if(handler.getMangaViewerFrame() != null && handler.currentScroll != 0){
						
						handler.yOffset = handler.getYOffset() + handler.currentScroll * SCROLLMULTIPLIER;
	
						
						if(handler.currentScroll > 0){
							//down
							handler.currentScroll -= 1;
						}else{
							//up
							handler.currentScroll += 1;
						}
						
						handler.getMangaViewerFrame().repaint();
					}
					if(handler.updateImages && !updating){		
						updating = true;
						if(handler.wasUpdateNext){
							new Thread(() -> {
								handler.loadNext();	
							}).start();				
							
						}else{
							
							new Thread(() -> {
								handler.loadPrevious();
							}).start();
							
							
						}
						handler.updateImages = false;
						updating = false;
					}			
					lastTime = System.currentTimeMillis();
				
			}
		}	
		
	}
	
	public static class MangaImageLoaderThread extends Thread{
		
		private MangaViewerHandler handler;
		
		public MangaImageLoaderThread(MangaViewerHandler handler){
			this.handler = handler;
		}		
		
		@Override
		public void run(){
			long lastTime = System.currentTimeMillis();
			
			long lastImageTime = System.currentTimeMillis();
			
			boolean updating = false;
			while(true){
				
				if((System.currentTimeMillis() > lastImageTime + MangaHereManga.CONNECTIONDELAY)){
					if(handler.onlineManga == null) continue;
					MangaHereManga mangaHereManga = handler.onlineManga;
					Manga manga  = handler.getCurrentManga();
					
					MangaImage mImage = mangaHereManga.getLastMangaImage();
					if(mImage == null) continue;
					
					int chapter = mImage.getChapter();
					int page = mImage.getPage();
					
					if(chapter - 1 > handler.getCurrentChapter()){
						
						continue;
					}
					
					if(page < manga.getPages(chapter) - 1){
						handler.getImage(manga, chapter, page + 1);
					}else if(chapter < manga.getTotalChapters()){
						if(!(chapter + 1 >= manga.getTotalChapters())){
							handler.getImage(manga, chapter + 1, 0);
						}
					}
					
					
					
					
					
					lastImageTime = System.currentTimeMillis();
				}
				
				if((System.currentTimeMillis() > lastTime + 17)){				
					if(handler.getMangaViewerFrame() != null && handler.currentScroll != 0){
						
						handler.yOffset = handler.getYOffset() + handler.currentScroll * SCROLLMULTIPLIER;
	
						
						if(handler.currentScroll > 0){
							//down
							handler.currentScroll -= 1;
						}else{
							//up
							handler.currentScroll += 1;
						}
						
						handler.getMangaViewerFrame().repaint();
					}
					if(handler.updateImages && !updating){		
						updating = true;
						if(handler.wasUpdateNext){
							new Thread(() -> {
								handler.loadNext();	
							}).start();				
							
						}else{
							
							new Thread(() -> {
								handler.loadPrevious();
							}).start();
							
							
						}
						handler.updateImages = false;
						updating = false;
					}			
					lastTime = System.currentTimeMillis();
				}
			}
		}	
	}
	
	
	
	
	public void reload(){
		loadImages();
		if(this.getMangaViewerFrame() != null)
			this.getMangaViewerFrame().repaint();
	}
	
	
	private boolean loading = false;
	
	private void loadPrevious(){
		if(loading) return;
		loading = true;
		if(currentPage > 0){
			previousImage = getImage(currentManga, currentChapter, currentPage - 1);
			//System.out.println("Loaded previous");
		}else if(currentChapter > 0){
			previousImage = getImage(currentManga, currentChapter - 1, currentManga.getPages(currentChapter - 1) - 1);
			//System.out.println("Loaded previous in previous chapter");			
		}

		loading = false;
	}
	
	private void loadCurrent(){
		if(loading) return;
		loading = true;

		currentImage = getImage(currentManga, currentChapter, currentPage);
		//System.out.println("Loaded current");

		loading = false;
	}
	
	private void loadNext(){
		if(loading) return;
		loading = true;

		if(currentPage < currentManga.getPages(currentChapter) - 1){
			nextImage = getImage(currentManga, currentChapter, currentPage + 1);
			//System.out.println("Loaded next");
		}else if(currentChapter < currentManga.getTotalChapters()){
			if(!(currentChapter + 1 >= currentManga.getTotalChapters())){
				nextImage = getImage(currentManga, currentChapter + 1, 0);
			}else{
				nextImage = null;				
			}
			//System.out.println("Loaded next in next chapter");
		}

		loading = false;

	}
	
	
	private void loadImages(){
		loadCurrent();
		loadPrevious();
		loadNext();		
	}
	
	private BufferedImage getImage(Manga manga, int chapter, int page){		
		
		if(manga instanceof FileManga){
			
			
			FileMangaChapter mangaChapter = ((FileManga) manga).getChapters().get(chapter);
			
			String imageName = mangaChapter.getImageFileNames().get(page);	
			
			BufferedImage image = null;		
			try {
				
				if(currentMangaFile.isDirectory()){				
					String path = currentMangaFile.toString() + "\\" + mangaChapter.getName() + "\\" + mangaChapter.getImageFileNames().get(page);
					image = ImageIO.read(new File(path));
				}else{
					InputStream stream = zipFile.getInputStream(zipFile.getEntry(imageName));			
					image = ImageIO.read(stream);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}else if(manga instanceof OnlineManga){
			
			
			BufferedImage image = onlineManga.getImage(chapter, page);
			
			return image;
		}
		return null;
	}
	
	public void nextPage(){
		if(updateImages || loading || currentManga == null) return;
		if(currentPage + 1 >= currentManga.getPages(currentChapter) && currentManga.getTotalChapters() <= currentChapter + 1) return;
		currentPage++;
		currentScroll = 0;
		
		
		if(currentPage >= currentManga.getPages(currentChapter)){
			currentPage = 0;
			currentChapter += 1;
		}
		previousImage = currentImage;
		currentImage = nextImage;
		
		if(this.onlineManga != null){
			this.onlineManga.setLoadedChapter(currentChapter);
			this.onlineManga.setLoadedPage(currentPage);
		}		
		MangaProperties.saveOnlineMangas();
		
		
		yOffset = 0;
		
		getMangaViewerFrame().updateTitle();
		
		properties.saveProperties();
		
		
		this.getMangaViewerFrame().repaint();
		
		
		wasUpdateNext = true;
		updateImages = true;
	}
	
	public void previousPage(){
		if(updateImages || loading || currentManga == null) return;
		if(currentPage <= 0 && currentChapter == 0) return;
		currentPage--;
		currentScroll = 0;
		
		if(currentPage < 0){
			currentChapter -= 1;
			currentPage = currentManga.getPages(currentChapter) - 1;			
		}
		
		nextImage = currentImage;
		currentImage = previousImage;
		
		if(this.onlineManga != null){
			this.onlineManga.setLoadedChapter(currentChapter);
			this.onlineManga.setLoadedPage(currentPage);
		}
		MangaProperties.saveOnlineMangas();

		yOffset = 0;
		getMangaViewerFrame().updateTitle();

		properties.saveProperties();
		
		this.getMangaViewerFrame().repaint();
		
		wasUpdateNext = false;
		updateImages = true;
		
	}
	
	public void loadManga(File file){
		
		this.currentMangaFile = file;

		if(file.isDirectory()){
			
			ArrayList<FileMangaChapter> chapters = new ArrayList<FileMangaChapter>();
			ArrayList<String> imageFileNames = new ArrayList<String>();

			
			for(File f : file.listFiles()){
				imageFileNames = new ArrayList<String>();	
				
				String chapterName = f.getName();
				
				for(File img : f.listFiles()){
					imageFileNames.add(img.getName());
				}
				
				chapters.add(new FileMangaChapter(chapterName, imageFileNames));				
			}
			
			currentManga = new FileManga(file.getName(), chapters);		

			
		}else{
			
			try {
				zipFile = new ZipFile(file);

				Enumeration<? extends ZipEntry> entries = zipFile.entries();			
				ArrayList<FileMangaChapter> chapters = new ArrayList<FileMangaChapter>();
				
				String chapterName = null;
				ArrayList<String> imageFileNames = new ArrayList<String>();
				
				while(entries.hasMoreElements()){
					ZipEntry entry = entries.nextElement();
					
					if(entry.isDirectory()){
						if(chapterName == null){
							chapterName = entry.getName();
						}else{
							chapters.add(new FileMangaChapter(chapterName, imageFileNames));
							
							chapterName = entry.getName();
							imageFileNames = new ArrayList<String>();					
						}
					}else{					
						imageFileNames.add(entry.getName());					
					}			
				}			
				chapters.add(new FileMangaChapter(chapterName, imageFileNames));				
				currentManga = new FileManga(file.getName().substring(0, file.getName().lastIndexOf('.')), chapters);		
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		properties = new MangaProperties(this, currentManga);

		this.currentChapter = properties.getChapter();
		this.currentPage = properties.getPage();
		
		if(this.getMangaViewerFrame() != null)
			this.getMangaViewerFrame().updateTitle();
		loadImages();
	}
	
	public MangaFitType getImageFitType(BufferedImage image){
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
				
		if(1.0 * imageWidth/imageHeight < MangaViewerHandler.SCROLLRATIO){
			return MangaFitType.SCROLL;
		}else if(this.getMangaViewerPanel().getWidth() < imageWidth){
			return MangaFitType.WIDTH;
		}
		
		return MangaFitType.HEIGHT;		
	}
	
	public MangaProperties getProperties(){
		return properties;
	}
	
	public void setCurrentChapter(int chapter){
		this.currentChapter = chapter;
	}
	
	public void setCurrentPage(int page){
		this.currentPage = page;
	}
	
	public double getScrollWidthMultiplier(){
		return scrollWidthMultiplier;
	}
	
	public void setScrollWidthMultiplier(double mult){
		this.scrollWidthMultiplier = mult;
	}
	
	public File getMangaFolder(){
		return mangaFolder;
	}

	public Manga getCurrentManga(){
		return currentManga;
	}

	public BufferedImage getCurrentImage(){
		return currentImage;
	}
	
	public MangaViewerFrame getMangaViewerFrame(){
		return frame;
	}
	
	public MangaViewerPanel getMangaViewerPanel(){
		return panel;
	}
	
	public int getScreenWidth(){
		return screenWidth;
	}
	
	public int getScreenHeight(){
		return screenHeight;
	}
	
	public int getYOffset(){
		return yOffset;
	}
	
	public void setYOffset(int yOffset){
		this.yOffset = yOffset;
	}

	public int getCurrentChapter() {
		return currentChapter;
	}
	
	public int getCurrentPage(){
		return currentPage;		
	}
	
	public int getCurrentScroll(){
		return currentScroll;
	}
	
	public void setCurrentScroll(int scroll){
		this.currentScroll = scroll;
	}
	
	public MangaHereManga getOnlineManga(){
		return onlineManga;
	}
	
}
