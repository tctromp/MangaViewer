package org.trompgames.mangaviewer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.trompgames.onlinemanga.OnlineManga;

public class MangaViewerFrame extends JFrame{

	private MangaViewerHandler handler;
	private MangaViewerPanel panel;
	
	
	public MangaViewerFrame(MangaViewerHandler handler, int width, int height) {
		this.handler = handler;
				
		this.setSize(width/2, height);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.setLocation(0, 0);
		
		JFileChooser fc = new JFileChooser(handler.getMangaFolder());

		this.add(fc);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		fc.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File file) {
				if(file.getName().endsWith(".zip") || file.getName().endsWith(".cbz") || file.isDirectory()) return true;
				return false;
			}

			@Override
			public String getDescription() {
				return "File types: zip, cbz, or directory";
			}
			
		});
		
		
		fc.showOpenDialog(this);
		
		File file = fc.getSelectedFile();
		
		if(file == null){
			System.exit(0);
		}
		
		
		
		//Add panel
		panel = new MangaViewerPanel(handler);
		this.add(panel);
		
		
		//Add Listeners
		addMouseWheelListener();
		addMouseListener();
		addKeyListener();
		
		//Set close operation and show frame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		handler.setCurrentMangaFile(file);
		
	}
	
	public void updateTitle(){
		Manga m = handler.getCurrentManga();
		
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
			
			this.setTitle("MangaViewer - " + chapter.getName() + " - Page: (" + (handler.getCurrentPage() + 1) + "/" + chapter.getPages() + ") - " + cumPages + "/" + manga.getTotalPages() + " - " + s);
		}else{
			
			OnlineManga manga = (OnlineManga) m;
			
			this.setTitle("MangaViewer - " +  " - Page: (" + (handler.getCurrentPage() + 1) + "/" + manga.getPages(handler.getCurrentChapter()) + ")");
		}

		
	}
	
	public void addKeyListener(){
		this.addKeyListener(new KeyListener(){

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
				 * 
				 */				
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
				
				}			
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void addMouseListener(){
		
		this.addMouseListener(new MouseListener(){

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
		this.addMouseWheelListener(new MouseWheelListener(){
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
	
}
