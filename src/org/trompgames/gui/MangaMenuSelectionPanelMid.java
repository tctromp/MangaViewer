package org.trompgames.gui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.trompgames.mangainfo.MangaCategory;
import org.trompgames.onlinemanga.MangaHereManga;

public class MangaMenuSelectionPanelMid extends JPanel{

	private MangaMenuSelectionPanel panel;
	
	private JPanel topRightPanel; 
	
	
	public MangaMenuSelectionPanelMid(MangaMenuSelectionPanel panel) {
		this.panel = panel;
		
		
		this.setLayout(new GridLayout(0, 2));
		//midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.LINE_AXIS));

		JPanel topLeftPanel = new JPanel();
		topRightPanel = new JPanel();
		
		
		topLeftPanel.setBorder(BorderFactory.createTitledBorder("Info: "));
		topRightPanel.setBorder(BorderFactory.createTitledBorder("Cover: "));
		
		
		topLeftPanel.setLayout(new GridLayout(0, 1));
		
		
		panel.setDetailField(new JTextArea());
		JTextArea detailField = panel.getDetailField();
		
		/*
		 * loadMidPanelData(manga);
		 */

		
		detailField.setHighlighter(null);
		detailField.setEditable(false);
		
		detailField.setLineWrap(true);
		detailField.setWrapStyleWord(true);
		
		detailField.setFont(new Font(detailField.getFont().getFontName(), 0, 18));
		detailField.setBackground(null);
		
		topLeftPanel.add(detailField);
		
		topRightPanel.setLayout(new GridLayout(0, 1));
		
		this.add(topLeftPanel);
		this.add(topRightPanel);
		panel.add(this);
	}
	
	public void loadCover(MangaHereManga manga){
		if(manga.getCover() != null)
			topRightPanel.add(new JLabel(new ImageIcon(manga.getCover())));	
	}
	
	public void loadData(MangaHereManga manga){

		String details = "";
		details += "Title: " + manga.getTitle() + "\n";
		details += "Rating: " + manga.getRating() + "\n\n";
		details += "Genres: ";
		
		for(int i = 0; i < manga.getGenres().size() - 1; i++){
			MangaCategory mc = manga.getGenres().get(i);
			details += mc.toString() + ", ";
		}
		details += manga.getGenres().get(manga.getGenres().size() - 1);
		
		details +=  "\n\n";
		
		details += "Author(s): " + manga.getAuthor() + "\n";
		details += "Artist(s): " + manga.getArtist() + "\n\n";
		details += "Status: " + manga.getStatus() + "\n";
		details += "Rank: " + manga.getRank() + "\n";
		panel.getDetailField().setText(details);
	}
	
	
}
