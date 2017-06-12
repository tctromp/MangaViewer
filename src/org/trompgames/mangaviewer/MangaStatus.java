package org.trompgames.mangaviewer;

public enum MangaStatus {

	COMPLETED("Completed"),
	ONGOING("Ongoing"),
	NEW("New");
	
	private String name;
	
	private MangaStatus(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public static MangaStatus getStatus(String s){
		for(MangaStatus ms : MangaStatus.values()){
			if(s.contains(ms.toString())) return ms;
		}
		return null;
	}
	
}
