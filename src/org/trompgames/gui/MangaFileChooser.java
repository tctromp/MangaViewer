package org.trompgames.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.trompgames.mangaviewer.MangaViewerHandler;

public class MangaFileChooser extends JFileChooser{

	private MangaViewerHandler handler;
	
	public MangaFileChooser(File folder, MangaViewerHandler handler) {
		super(folder);
		this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		this.setFileFilter(new FileFilter(){

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
		
		MangaFileChooser mangaFileChooser = this;
		
		this.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {				
				handler.loadManga(mangaFileChooser.getSelectedFile());
				handler.reload();
			}			
		});
		
	}

	public MangaViewerHandler getHandler() {
		return handler;
	}
	
	
	
}
