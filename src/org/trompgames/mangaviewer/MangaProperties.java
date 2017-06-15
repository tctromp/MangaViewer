package org.trompgames.mangaviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.trompgames.onlinemanga.MangaHereManga;
import org.trompgames.onlinemanga.OnlineMangaChapter;

public class MangaProperties {

	private File file = new File("mangaData");

	//private Properties properties;
	
	private MangaViewerHandler handler;
	private Manga manga;
	
	private int chapter = 0;
	private int page = 0;
	
	
	
	public MangaProperties(MangaViewerHandler handler, Manga manga){
		this.handler = handler;
		this.manga = manga;
		loadProperties();		
	}
	
	public Properties loadProperties(){		
		
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			fileInputStream.close();
			
			
			Enumeration e = properties.keys();
			while(e.hasMoreElements()){				
				String key = (String) e.nextElement();
				String value = properties.getProperty(key);				

				if(key.equals("manga." + manga.getName() + ".chapter")){
					
					this.chapter = Integer.parseInt(value);
					
				}else if(key.equals("manga." + manga.getName() + ".page")){
					
					this.page = Integer.parseInt(value);
					
				}
				
			}
			
			return properties;
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public void loadJSON(MangaHereManga onlineManga){
		
		JSONParser parser = new JSONParser();
		
		System.out.println("starting");
		
		try {
			FileReader fr = new FileReader("mangaData.json");
			JSONObject jsonObject = (JSONObject) parser.parse(fr);		
			
			JSONArray mangas = (JSONArray) jsonObject.get("mangas");
			
			ListIterator li = mangas.listIterator();
			
			while(li.hasNext()){
				Object next = li.next();
				if(next instanceof JSONObject){
					JSONObject jo = (JSONObject) next;
					
					if(jo.get("title") == null) continue;
					String title = (String) jo.get("title");
					if(title != null && title.equalsIgnoreCase(onlineManga.getTitle())){

						
					
					}					
				}
				
			}	
			
			fr.close();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static ArrayList<MangaHereManga> getOnlineMangas(){
		
		JSONParser parser = new JSONParser();
		
		System.out.println("starting");
		
		ArrayList<MangaHereManga> mangaHereMangas = new ArrayList<>();
		
		try {
			FileReader fr = new FileReader("mangaData.json");
			JSONObject jsonObject = (JSONObject) parser.parse(fr);		
			
			JSONArray mangas = (JSONArray) jsonObject.get("mangas");
			
			
			
			for(int i = 0; i < mangas.size(); i++){
				Object next = mangas.get(i);
				if(next instanceof JSONObject){
					JSONObject jo = (JSONObject) next;
					
					String title = (String) jo.get("title");
					int currentChapter = Integer.parseInt((String) jo.get("currentChapter"));
					int currentPage = Integer.parseInt((String) jo.get("currentPage"));
					double rating = Double.parseDouble((String) jo.get("rating"));

					String author = (String) jo.get("author");
					String artist = (String) jo.get("artist");
					MangaStatus status = MangaStatus.getStatus((String) jo.get("status"));
					int rank = Integer.parseInt((String) jo.get("rank"));
					String urlString = (String) jo.get("url");

					//Genres and Chapters
					
					ArrayList<MangaCategory> genres = getGenres((JSONArray) jo.get("genres"));
					ArrayList<OnlineMangaChapter> chapters = getChapters((JSONArray) jo.get("chapters"));
					
					mangaHereMangas.add(new MangaHereManga(urlString, title, rating, genres, author, artist, status, rank, chapters, currentChapter, currentPage));
					
				}
				
			}	
			
			fr.close();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return mangaHereMangas;
		
	}
	
	private static ArrayList<MangaCategory> getGenres(JSONArray genres){
		ArrayList<MangaCategory> mangaCategories = new ArrayList<>();
		
		for(int i = 0; i < genres.size(); i++){
			String s = (String) genres.get(i);
			mangaCategories.add(MangaCategory.getCategory(s));			
		}
		return mangaCategories;
		
	}
	
	private static ArrayList<OnlineMangaChapter> getChapters(JSONArray chapters){
		
		ArrayList<OnlineMangaChapter> mangaChapters = new ArrayList<>();
		
		for(int i = 0; i < chapters.size(); i++){
			
			JSONObject obj = (JSONObject) chapters.get(i);
			
			String baseURL = (String) obj.get("baseURL");
			String name = (String) obj.get("name");
			String details = (String) obj.get("details");
			
			mangaChapters.add(new OnlineMangaChapter(name, details, i, baseURL, null, chapters.size()));
			
		}
		
		return mangaChapters;
		
	}
	
	//private MangaHereManga getOnlineManga(JSONObject jo){
		
	//	String URLString = (String) jo.get("URLString");
		
		
	//}
	
	@SuppressWarnings("unchecked")
	public void saveProperties(){
		
		
		Properties properties = loadProperties();
		
		properties.setProperty("manga." + manga.getName() + ".chapter", "" + handler.getCurrentChapter());
		properties.setProperty("manga." + manga.getName() + ".page", "" + handler.getCurrentPage());
		
		if(handler.getOnlineManga() != null){
			
			MangaHereManga onlineManga = handler.getOnlineManga();

			
			
			

		}
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			properties.store(out, "Manga Data");			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static void saveOnlineMangas(){
		JSONObject obj = new JSONObject();
		
		JSONArray mangas = new JSONArray();
		
		ArrayList<MangaHereManga> mangaHereMangas = getOnlineMangas();
		
		for(MangaHereManga m : mangaHereMangas){
			mangas.add(getMangaJSONObject(m));
		}
		
		obj.put("mangas", mangas);
		
		
		try {
			FileWriter fw = new FileWriter("mangaData.json");
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void addManga(MangaHereManga manga){
		JSONObject obj = new JSONObject();
		
		JSONArray mangas = new JSONArray();
		
		ArrayList<MangaHereManga> mangaHereMangas = getOnlineMangas();
		
		for(MangaHereManga m : mangaHereMangas){
			mangas.add(getMangaJSONObject(m));
		}
		mangas.add(getMangaJSONObject(manga));
		obj.put("mangas", mangas);
		
		
		try {
			FileWriter fw = new FileWriter("mangaData.json");
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getMangaJSONObject(MangaHereManga manga){
		JSONObject mangaDetails = new JSONObject();
		
		
		mangaDetails.put("title", manga.getTitle());
		mangaDetails.put("currentChapter", "" + manga.getLoadedChapter());
		mangaDetails.put("currentPage", "" + manga.getLoadedPage());
		mangaDetails.put("rating", "" + manga.getRating());

		JSONArray genres = new JSONArray();
		for(MangaCategory cat : manga.getGenres()){
			genres.add(cat.toString());
		}
		mangaDetails.put("genres", genres);
		
		mangaDetails.put("author", manga.getAuthor());
		mangaDetails.put("artist", manga.getArtist());

		mangaDetails.put("status", manga.getStatus().toString());
		mangaDetails.put("rank", "" + manga.getRank());
		
		mangaDetails.put("url", manga.getUrlString());
		
		JSONArray chapters = new JSONArray();
		
		for(int i = 0; i < manga.getTotalChapters(); i++){
			
			OnlineMangaChapter ch = manga.getOnlineMangaChapters().get(i);
			
			String baseURL = ch.getBaseURL();
			String name = ch.getName();
			String details = ch.getDetails();
			
			JSONObject chapter = new JSONObject();
			chapter.put("baseURL", baseURL);
			chapter.put("name", name);
			chapter.put("details", details);
			chapters.add(chapter);
		}
		mangaDetails.put("chapters", chapters);
		return mangaDetails;
	}
	
	
	

	public int getChapter() {
		return chapter;
	}

	public int getPage() {
		return page;
	}
	
}
