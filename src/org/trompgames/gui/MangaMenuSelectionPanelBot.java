package org.trompgames.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.trompgames.mangaloaing.MangaProperties;
import org.trompgames.onlinemanga.MangaHereManga;
import org.trompgames.onlinemanga.OnlineMangaChapter;

public class MangaMenuSelectionPanelBot extends JPanel{

	private MangaMenuSelectionPanel panel;
	
	public MangaMenuSelectionPanelBot(MangaMenuSelectionPanel panel){
		this.panel = panel;
		
		
		JList<String> chapterList = panel.getChapterList();
		
		this.setBorder(BorderFactory.createTitledBorder("Chapters: "));
		this.setLayout(new GridLayout(0, 1));
		
		
		/*
		 * loadBotPanelData(manga);				
		 */
		
		chapterList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		//list.setVisibleRowCount(-1);
		chapterList.setFont(new Font(chapterList.getFont().getName(), 0, 20));
		chapterList.setBackground(null);

		JScrollPane listScroller = new JScrollPane(chapterList);		
		
		
		listScroller.getViewport().getView().addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent event) {
				//System.out.println("Clicky");
			}			
			
		});
		
		this.add(listScroller);
		panel.add(this);			
		
		
	}
	
	public void loadData(MangaHereManga manga){
		MangaProperties properties = new MangaProperties(panel.getHandler(), manga.getManga());
		
		int currentChapter = properties.getChapter();
		int currentPage = properties.getPage();				
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for(int i = 0; i < manga.getOnlineMangaChapters().size(); i++){
			OnlineMangaChapter ch = manga.getOnlineMangaChapters().get(i);
			String name = ch.getName();
			String detail = ch.getDetails();
			
			String currentString = ((currentChapter == i) && (currentPage != 0)) ? "(Current) " : "";
			
			
			
			if(!detail.equals("")){
				model.addElement(currentString + name + " - " + detail);
			}else{
				model.addElement(currentString + name);
			}
			
		}
		
		JList<String> chapterList = panel.getChapterList();
		
		chapterList.setModel(model);
		chapterList.setSelectedIndex(currentChapter);
	}
	
	
}
