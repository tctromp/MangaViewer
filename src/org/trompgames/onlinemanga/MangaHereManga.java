package org.trompgames.onlinemanga;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.trompgames.mangaviewer.Manga;
import org.trompgames.mangaviewer.MangaCategory;
import org.trompgames.mangaviewer.MangaStatus;

public class MangaHereManga {

	public static final int CONNECTIONDELAY = 250;
	
	private Manga manga;
	private String urlString;
	
	
	private String title;
	private BufferedImage cover;
	private double rating;
	private ArrayList<MangaCategory> genres = new ArrayList<MangaCategory>();
	private String author;
	private String artist;	
	private MangaStatus status;
	private int rank;
	
	private ArrayList<OnlineMangaChapter> onlineMangaChapters = new ArrayList<OnlineMangaChapter>();
	
	private ArrayList<String> chapterURLS = new ArrayList<String>();
	private ArrayList<String> chapterNames = new ArrayList<String>();
	private ArrayList<String> chapterDetails = new ArrayList<String>();
	
	private ArrayList<String> pageContents = new ArrayList<String>();
	
	public MangaHereManga(String urlString){
		this.urlString = urlString;		
		
		loadHtml();        
		
		loadTitle();
		System.out.println("Title: " + title);
		
		
		loadCover();
		
		
		loadRating();
		System.out.println("Rating: " + rating);
		
		loadGenres();
		System.out.print("Genres: ");
		for(MangaCategory mc : genres){
			System.out.print(mc + " ");
		}
		System.out.println();
		
		
		loadAuthor();
		System.out.println("Author: " + author);
		
		
		loadArtist();
		System.out.println("Artist: " + artist);
		
		
		
		loadStatus();
		System.out.println("Status: " + status);
		
		
		loadRank();
		System.out.println("Rank: " + rank);
		
		
		loadChapters();
		//System.out.println("Chapters: ");
		
		for(int i = 0; i < chapterURLS.size(); i++){
			//String url = chapterURLS.get(i);
			//String detail = chapterDetails.get(i);
			//System.out.println(url + " - " + detail);			
		}		
		
		//delay(5000);
			
		
		
		manga = new OnlineManga(title, this);
		
		//getImage(1, 1);
	}
	
	private void delay(long mills){
		long time = System.currentTimeMillis();
		
		while(System.currentTimeMillis() < time + mills){
			
		}
	}

	private void loadHtml(){
		try {
			URL url = new URL(urlString);			
			 BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));		        
		        String inputLine;
		        while ((inputLine = in.readLine()) != null){
		        	pageContents.add(inputLine);
		        }		        
		        in.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTitle(){		
		for(String line : pageContents){
			if(line.contains("<h1 class=\"title\"><span class=\"title_icon\"></span>")){
				String s = line.replace("<h1 class=\"title\"><span class=\"title_icon\"></span>", "");
				s = s.replace("</h1>", "");
				s = s.trim();
				title = s;
				return;
			}			
		}		
	}
	
	private void loadCover(){
		for(String line : pageContents){
			if(line.contains("<img src=\"http://h.mhcdn.net/store/manga/")){
				String s = line.replace("<img src=\"", "");
				s = s.substring(0, s.indexOf('"'));
				s = s.trim();
				
				try {
					cover = ImageIO.read(new URL(s));
				} catch (IOException e) {
					e.printStackTrace();
				}				
				return;
			}				
		}		
	}
	
	private void loadRating(){
		for(String line : pageContents){
			if(line.contains("<span id=\"current_rating\" class=\"scores\">")){
				String s = line.replace("<span id=\"current_rating\" class=\"scores\">", "");
				s = s.trim();
				String r = "" + s.charAt(0);
				s = s.replaceFirst(r, "");
				s = s.replaceFirst("<em>", "");
				r += s.substring(0, 3);
				rating = Double.parseDouble(r);
				return;
			}			
		}		
	}
	
	//If this is broken for some reason check if the genres are on multiple lines in the HTML
	private void loadGenres(){
		for(String line : pageContents){
			if(line.contains("<li><label>Genre(s):</label>")){
				String s = line.replace("<li><label>Genre(s):</label>", "");
				s = s.replace("</li>", "");
				s = s.trim();
				
				StringTokenizer st = new StringTokenizer(s, ",");
				
				String token = "";
				while(st.hasMoreTokens()){
					token = st.nextToken();
					token = token.trim();
					
					MangaCategory mc = MangaCategory.getCategory(token);
					if(mc == null){
						System.out.println("ERROR: Invalid/New manga category");
						continue;
					}
					genres.add(mc);					
				}				
				return;
			}			
		}		
	}
	
	private void loadAuthor(){
		for(String line : pageContents){
			if(line.contains("<li><label>Author(s):</label>")){
				String s = line.replace("</a></li>", "");
				s = s.substring(s.lastIndexOf('>') + 1, s.length());
				s = s.trim();
				author = s;
				return;
			}			
		}	
	}
	
	private void loadArtist(){
		for(String line : pageContents){
			if(line.contains("<li><label>Artist(s):</label>")){
				String s = line.replace("</a></li>", "");
				s = s.substring(s.lastIndexOf('>') + 1, s.length());
				s = s.trim();
				artist = s;
				return;
			}			
		}	
	}
	
	private void loadStatus(){
		for(String line : pageContents){
			if(line.contains("<li><label>Status:</label>")){
				String s = line.replace("<li><label>Status:</label>", "");
				s = s.replace("</li>", "");
				s = s.trim();
				status = MangaStatus.getStatus(s);
				return;
			}			
		}		
	}
	
	private void loadRank(){
		for(String line : pageContents){
			if(line.contains("<li><label>Rank:</label>")){
				String s = line.replace("<li><label>Rank:</label>", "");
				s = s.replace("</li>", "");
				s = s.trim();
				s = s.substring(0, s.length()-2);
				rank = Integer.parseInt(s);
				return;
			}			
		}		
	}

	private void loadChapters(){
		boolean getInfo = false;
		boolean skip = false;
		for(String line : pageContents){
			if(line.trim().indexOf("<a class=\"color_0077\" href=\"http://www.mangahere.co/manga") == 0){
				String s = line.replace("<a class=\"color_0077\" href=\"", "");
				s = s.substring(0, s.indexOf("\" "));
				s = s.trim();
				chapterURLS.add(0, s);
				getInfo = true;
				skip = true;
			}else if(getInfo && skip == false){
				String s = line.replace("<span class=\"mr6\"></span>", "");
				s = s.replace("</span>", "");
				s = s.trim();
				chapterDetails.add(0, s);
				getInfo = false;
			}else{
				skip = false;
			}
		}		
	}
	
	public BufferedImage getImage(int chapter, int page){
		
		OnlineMangaChapter c = getOnlineMangaChapter(chapter);
		
		ArrayList<String> imageBaseURLS;
		
		if(c == null){		
			imageBaseURLS = getChapterImageBaseURLS(chapter);
			onlineMangaChapters.add(new OnlineMangaChapter("temp", chapterDetails.get(chapter), chapter, imageBaseURLS));
		}else{
			imageBaseURLS = c.getBaseURLS();
		}		
		try {
			return getImage(new URL(imageBaseURLS.get(page)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage getImage(URL url){
		delay(CONNECTIONDELAY);
		
		ArrayList<String> pageLines = new ArrayList<String>();	
		
		try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));		        
		     String inputLine;
		     while ((inputLine = in.readLine()) != null){
		      	pageLines.add(inputLine);
		     }		        
		     in.close();			
		} catch (IOException e) {
			
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
			    while ((inputLine = in.readLine()) != null){
			     	pageLines.add(inputLine);
			    }		        
			     in.close();	
			} catch (IOException e1) {
				e1.printStackTrace();
			}		        
		    
		}
		for(String line : pageLines){
			if(line.contains("<img src=\"http://h.mhcdn.net/store/manga/")){
				String s = line.replace("<img src=\"", "");
				s = s.substring(0, s.indexOf('"'));
				s = s.trim();
				
				try {
					return ImageIO.read(new URL(s));
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}	
		
		return null;
	}
	
	public ArrayList<String> getChapterImageBaseURLS(int chapter){
		delay(CONNECTIONDELAY);
		
		ArrayList<String> pageLines = new ArrayList<String>();		
		
		try {
			URL url = new URL(chapterURLS.get(chapter));			
			 BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));		        
		        String inputLine;
		        while ((inputLine = in.readLine()) != null){
		        	pageLines.add(inputLine);
		        }		        
		        in.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> imageBaseURLS = new ArrayList<String>();
		
        boolean firstGroupEnd = false;
        boolean foundFirstGroup = false;
		
		for(String line : pageLines){
			if(line.contains("<option value=\"http://www.mangahere.co/manga") && (!foundFirstGroup || !firstGroupEnd)){
				String s = line.replace("<option value=\"", "");
				s = s.substring(0, s.indexOf('"'));
				s = s.trim();
				imageBaseURLS.add(s);
				foundFirstGroup = true;
				
			}else if(foundFirstGroup){
				firstGroupEnd = true;
			}		
		}		
		return imageBaseURLS;
	}
	
	public int getPages(int chapter){		
		OnlineMangaChapter c = getOnlineMangaChapter(chapter);		
		ArrayList<String> imageBaseURLS;		
		if(c == null){		
			imageBaseURLS = getChapterImageBaseURLS(chapter);
			onlineMangaChapters.add(new OnlineMangaChapter("temp", chapterDetails.get(chapter), chapter, imageBaseURLS));
		}else{
			imageBaseURLS = c.getBaseURLS();
		}		
		return imageBaseURLS.size();
	}
	
	public int getTotalChapters(){
		return chapterURLS.size();
	}
	


	public String getUrlString() {
		return urlString;
	}

	public String getTitle() {
		return title;
	}

	public BufferedImage getCover() {
		return cover;
	}

	public double getRating() {
		return rating;
	}

	public ArrayList<MangaCategory> getGenres() {
		return genres;
	}

	public String getAuthor() {
		return author;
	}

	public String getArtist() {
		return artist;
	}

	public MangaStatus getStatus() {
		return status;
	}

	public int getRank() {
		return rank;
	}

	public ArrayList<String> getChapterURLS() {
		return chapterURLS;
	}

	public ArrayList<String> getChapterNames() {
		return chapterNames;
	}

	public ArrayList<String> getChapterDetails() {
		return chapterDetails;
	}
	
	public Manga getManga(){
		return manga;
	}
	
	public OnlineMangaChapter getOnlineMangaChapter(int chapter){
		for(OnlineMangaChapter c : onlineMangaChapters){
			if(c.getChapter() == chapter) return c;
		}
		return null;		
	}
	
	//Chapters:
	//<a class="color_0077" href="http://www.mangahere.co/manga
	

	//private void
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
