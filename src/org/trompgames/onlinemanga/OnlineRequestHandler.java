package org.trompgames.onlinemanga;

import java.util.ArrayList;

import org.trompgames.mangaviewer.MangaViewerHandler;

public class OnlineRequestHandler {

	
	private ArrayList<OnlineRequest> requests = new ArrayList<>();
	
	private MangaViewerHandler handler;
	
	private OnlineRequestHandler(MangaViewerHandler handler){
		this.handler = handler;
	}
	
	public void addRequest(OnlineRequest request){
		this.requests.add(request);
	}
	
	public void update(){
		requests.get(0).run();
		requests.remove(0);
	}
	
	
	
	
}
