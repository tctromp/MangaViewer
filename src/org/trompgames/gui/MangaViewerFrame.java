package org.trompgames.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import org.trompgames.localmanga.FileManga;
import org.trompgames.mangabase.Manga;
import org.trompgames.mangabase.MangaChapter;
import org.trompgames.mangaviewer.MangaViewerHandler;
import org.trompgames.onlinemanga.OnlineManga;

public class MangaViewerFrame extends JFrame{

	private MangaViewerHandler handler;
	private MangaViewerPanel panel;	
	
	private MangaFileChooser mangaFileChooser;
	private MangaMenuBar mangaMenuBar;
	
	private boolean fullScreen = false;
	
	public void setFullscreen(boolean fullScreen){
		
		this.fullScreen = fullScreen;
		
		if(fullScreen){
			handler.getMangaViewerFrame().setExtendedState(JFrame.MAXIMIZED_BOTH); 
			handler.getMangaViewerFrame().dispose();
			handler.getMangaViewerFrame().setUndecorated(true);
			handler.getMangaViewerFrame().setVisible(true);
		}else{
			handler.getMangaViewerFrame().dispose();
			handler.getMangaViewerFrame().setUndecorated(false);
			handler.getMangaViewerFrame().setVisible(true);
		}
		
	}
	
	public void toggleFullscreen(){
		setFullscreen(!fullScreen);
	}
	
	public MangaViewerFrame(MangaViewerHandler handler, int width, int height) {
		this.handler = handler;
				
		//this.setSize(width/2, height);

		//this.setLocation(width/2, 0);
		
		this.setSize(width, height);

		this.setLocation(0, 0);
		
		
		
		mangaFileChooser = new MangaFileChooser(handler.getMangaFolder(), handler);
		this.add(mangaFileChooser);
		

		//Add panel
		panel = new MangaViewerPanel(handler, this);
		this.add(panel);
		
		
		
		//Add Listeners
		addMouseWheelListener();
		addMouseListener();
		addKeyListener();
		
		
		
		
		mangaMenuBar = new MangaMenuBar(handler);
		
		
				
		this.setJMenuBar(mangaMenuBar);	

		
		//Set close operation and show frame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//this.setUndecorated(true);
		
		this.setVisible(true);
		
		
		
	}
	
	public String getMangaFrameTitle(){
		Manga m = handler.getCurrentManga();
		
		if(m == null){
			return "";
		}
		
		if(m instanceof FileManga){
			FileManga manga = (FileManga) m;
			MangaChapter chapter = manga.getChapters().get(handler.getCurrentChapter());
			
			int cumPages = manga.getCumulativePages(handler.getCurrentPage(), handler.getCurrentChapter());
			
			double percent = 100.0 * cumPages/manga.getTotalPages();
			String s = "" + percent;
			
			if(s.indexOf('.') == 1) s = "0" + s;
			
			if(s.length() > 5){
				s = s.substring(0, 5);
			}else{
				for(int i = 0; i < 5 - s.length(); i++){
					s += "0";
				}
			}
			
			s += "%";
			
			return ("MangaViewer - " + chapter.getName() + " - Page: (" + (handler.getCurrentPage() + 1) + "/" + chapter.getPages() + ") - " + cumPages + "/" + manga.getTotalPages() + " - " + s);
		}else{
			
			OnlineManga manga = (OnlineManga) m;
			
			return ("MangaViewer - " + manga.getChapterName(handler.getCurrentChapter()) +  " - Page: (" + (handler.getCurrentPage() + 1) + "/" + manga.getPages(handler.getCurrentChapter()) + ")");
		}
	}
	
	public void updateTitle(){
		String title = getMangaFrameTitle();
		this.setTitle(title);
		this.mangaMenuBar.getTitleMenu().setText(title);
	}
	
	public void addKeyListener(){
		panel.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent event) {
				
				/*
				 * Up: 38
				 * Down: 40
				 * Left: 37
				 * Right: 39
				 * F11: 122
				 * Esc: 
				 */				
				//System.out.println(event.getKeyCode());
				
				switch(event.getKeyCode()){				
					case 38:
						//handler.setYOffset(handler.getYOffset() - 75);	
						
						handler.setCurrentScroll(handler.getCurrentScroll() - 15);
						
						//handler.getMangaViewerFrame().repaint();
						break;
						
					case 40:
						//handler.setYOffset(handler.getYOffset() + 75);		
						
						handler.setCurrentScroll(handler.getCurrentScroll() + 15);
						
						//handler.getMangaViewerFrame().repaint();
						break;
						
					case 37:
						handler.previousPage();
						break;
						
					case 39:
						handler.nextPage();
						break;
						
					case 122:
						handler.getMangaViewerFrame().toggleFullscreen();
						
						break;
				
				}			
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void addMouseListener(){
		
		panel.addMouseListener(new MouseListener(){

			//1 = left 
			//2 = middle
			//3 = right
			
			@Override
			public void mouseClicked(MouseEvent event) {		
				
			}

			@Override
			public void mousePressed(MouseEvent event) {
				switch(event.getButton()){
				
				case 1:
					handler.nextPage();
					break;
			
				case 2:
				
					break;
				
				case 3:
					handler.previousPage();

					break;
			
				}	
			}

			@Override
			public void mouseReleased(MouseEvent event) {				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}		
			
		});
		
	}
	
	public void addMouseWheelListener(){
		panel.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				int rot = event.getWheelRotation();
				handler.setYOffset(handler.getYOffset() + rot*75);				
				handler.getMangaViewerFrame().repaint();
				
			}			
		});
	}
	
	public MangaViewerPanel getMangaViewerPanel(){
		return panel;
	}



	public MangaFileChooser getMangaFileChooser() {
		return mangaFileChooser;
	}


	public MangaMenuBar getMangaMenuBar() {
		return mangaMenuBar;
	}
	
	
	
}
