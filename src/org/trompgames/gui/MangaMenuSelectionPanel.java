package org.trompgames.gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.trompgames.mangaloaing.MangaProperties;
import org.trompgames.mangaviewer.MangaViewerHandler;
import org.trompgames.onlinemanga.MangaHereManga;

public class MangaMenuSelectionPanel extends JPanel{

	private MangaViewerHandler handler;
	
	private JList<String> mangaList;
	private DefaultListModel<String> mangaListModel;
	private ArrayList<MangaHereManga> mangas;
	private JList<String> chapterList = new JList<String>();
	private JTextArea detailField;

	
	MangaMenuSelectionPanelTop topPanel;
	MangaMenuSelectionPanelMid midPanel;
	MangaMenuSelectionPanelBot botPanel;
	
	public MangaMenuSelectionPanel(MangaViewerHandler handler){
		this.handler = handler;
		this.setLayout(new GridLayout(3, 0));		
		this.mangas = MangaProperties.getOnlineMangas();
		
		
		this.topPanel = new MangaMenuSelectionPanelTop(this);
		this.midPanel = new MangaMenuSelectionPanelMid(this);
		this.botPanel = new MangaMenuSelectionPanelBot(this);

		this.midPanel.loadData(mangas.get(0));
		this.botPanel.loadData(mangas.get(0));
		
	}


	public JList<String> getMangaList() {
		return mangaList;
	}


	public void setMangaList(JList<String> mangaList) {
		this.mangaList = mangaList;
	}


	public DefaultListModel<String> getMangaListModel() {
		return mangaListModel;
	}


	public void setMangaListModel(DefaultListModel<String> mangaListModel) {
		this.mangaListModel = mangaListModel;
	}


	public ArrayList<MangaHereManga> getMangas() {
		return mangas;
	}


	public void setMangas(ArrayList<MangaHereManga> mangas) {
		this.mangas = mangas;
	}


	public MangaViewerHandler getHandler() {
		return handler;
	}


	public JList<String> getChapterList() {
		return chapterList;
	}


	public void setChapterList(JList<String> chapterList) {
		this.chapterList = chapterList;
	}


	public JTextArea getDetailField() {
		return detailField;
	}


	public void setDetailField(JTextArea detailField) {
		this.detailField = detailField;
	}


	public MangaMenuSelectionPanelTop getTopPanel() {
		return topPanel;
	}


	public MangaMenuSelectionPanelMid getMidPanel() {
		return midPanel;
	}


	public MangaMenuSelectionPanelBot getBotPanel() {
		return botPanel;
	}
	
	
	
	
}
