package org.trompgames.mangainfo;

public enum MangaCategory {
	
	ACTION("Action"),
	ADVENTURE("Adventure"),
	COMEDY("Comedy"),
	DOUJINSHI("Doujinshi"),
	DRAMA("Drama"),
	ECCHI("Ecchi"),
	FANTASY("Fantasy"),
	GENDERBENDER("Gender Bender"),
	HAREM("Harem"),
	HISTORICAL("Historical"),
	HORROR("Horror"),
	JOSEI("Josei"),
	MARTIALARTS("Martial Arts"),
	MATURE("Mature"),
	MECHA("Mecha"),
	MYSTERY("Mystery"),
	ONESHOT("One Shot"),
	PHYCHOLOGICAL("Phychological"),
	ROMANCE("Romance"),
	SCHOOLLIFE("School Life"),
	SCIFI("Sci-fi"),
	SEINEN("Seinen"),
	SHOUJO("Shoujo"),
	SHOUJOAI("Shoujo Ai"),
	SHOUNEN("Shounen"),
	SHOUNENAI("Shounen Ai"),
	SLICEOFLIFE("Slice of Life"),
	SPORTS("Sports"),
	SUPERNATURAL("Supernatural"),
	TRAGEDY("Tragedy"),
	YAOI("Yaoi"),
	YURI("Yuri");
	
	String name;
	
	private MangaCategory(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public static MangaCategory getCategory(String s){
		for(MangaCategory mc : MangaCategory.values()){
			if(mc.toString().equalsIgnoreCase(s)) return mc;
		}
		return null;
	}
	
}
