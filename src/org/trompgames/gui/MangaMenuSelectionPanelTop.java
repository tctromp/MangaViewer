package org.trompgames.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.trompgames.mangaloaing.MangaProperties;
import org.trompgames.mangaviewer.MangaViewerHandler;
import org.trompgames.onlinemanga.MangaHereManga;

public class MangaMenuSelectionPanelTop extends JPanel{

	
	public MangaMenuSelectionPanelTop(MangaMenuSelectionPanel panel){		
		
		panel.setMangaListModel(new DefaultListModel<String>());
		
		MangaViewerHandler handler = panel.getHandler();
		
		DefaultListModel<String> mangaListModel = panel.getMangaListModel();
		ArrayList<MangaHereManga> mangas = panel.getMangas();
		
		panel.setMangaList(new JList<String>(mangaListModel));
		JList<String> mangaList = panel.getMangaList();
		
		JList<String> chapterList = panel.getChapterList();
		
		
		
		for(MangaHereManga m : mangas){
			mangaListModel.addElement(m.getTitle());					
		}
		
		
		mangaList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		mangaList.setFont(new Font(mangaList.getFont().getName(), 0, 16));
		mangaList.setBackground(null);

		JScrollPane listScroller = new JScrollPane(mangaList);
		
		mangaList.setSelectedIndex(0);

		
		mangaList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent event) {
				MangaHereManga manga = mangas.get(panel.getMangaList().getSelectedIndex());
				
				panel.getMidPanel().loadData(manga);
				panel.getBotPanel().loadData(manga);
				//manga.loadCover();
				//panel.getMidPanel().loadCover(manga);
			}
			
			
			
		});
		
		
		
		
		
		this.add(listScroller);
		
		JButton button = new JButton("Read");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				
				handler.getMangaViewerFrame().remove(panel.getHandler().getMangaViewerFrame().getMangaMenuBar().getSplitPane());
				handler.getMangaViewerFrame().add(handler.getMangaViewerPanel());
				handler.getMangaViewerFrame().revalidate();
				
				handler.setCurrentChapter(chapterList.getSelectedIndex());
				
				
				handler.setOnlineManga(mangas.get(panel.getMangaList().getSelectedIndex()), chapterList.getSelectedIndex());
				
				
				handler.getMangaViewerPanel().requestFocusInWindow();
				//handler.reload();
			}				
			
		});
		
		JButton loadButton = new JButton("Load from URL");
		loadButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				
				String url = JOptionPane.showInputDialog(handler.getMangaViewerFrame(), "MangaHere URL: ", null);
				if(url == null) return;
				
				//String url = "http://www.mangahere.co/manga/uq_holder/";
				
				MangaHereManga manga = new MangaHereManga(url);				
				
				if(!manga.hasValidURL()){
					JOptionPane.showMessageDialog(handler.getMangaViewerFrame(), "Invalid URL", "Error: ", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				mangaListModel.addElement(manga.getTitle());
				mangas.add(manga);
				MangaProperties.addManga(manga);
				
			}				
			
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				
				handler.getMangaViewerFrame().remove(panel.getHandler().getMangaViewerFrame().getMangaMenuBar().getSplitPane());
				handler.getMangaViewerFrame().add(handler.getMangaViewerPanel());
				handler.getMangaViewerFrame().revalidate();				
				
				handler.getMangaViewerPanel().requestFocusInWindow();
				
			}				
			
		});
		
		
		this.add(button);
		this.add(loadButton);
		this.add(cancelButton);
		panel.add(this);
	}
	
}
