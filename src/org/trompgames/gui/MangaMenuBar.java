package org.trompgames.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import org.trompgames.mangaviewer.MangaViewerHandler;

public class MangaMenuBar extends JMenuBar{

	
	private JSplitPane splitPane;
	private JMenu titleMenu;
	
	public MangaMenuBar(MangaViewerHandler handler) {
		
		JMenu menu = new JMenu("Manga");
		menu.setFont(new Font(menu.getFont().getFontName(), 0, 16));
		
		
		JMenuItem openFileButton = new JMenuItem("Open");
		openFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				handler.getMangaViewerFrame().getMangaFileChooser().showOpenDialog(handler.getMangaViewerFrame());				
			}
			
		});
		
		/*
		 * NOTE:
		 * DOES NOT HAVE LIMITS SET YET
		 * 
		 * 
		 */
		
		JMenuItem nextChapterButton = new JMenuItem("Next Chapter");
		nextChapterButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {				
				handler.setCurrentChapter(handler.getCurrentChapter() + 1);		
				handler.setCurrentPage(0);
				handler.reload();
			}
			
		});
		
		JMenuItem previousChapterButton = new JMenuItem("Previous Chapter");
		previousChapterButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				handler.setCurrentChapter(handler.getCurrentChapter() - 1);	
				handler.setCurrentPage(0);
				handler.reload();

			}
			
		});
		
		JMenuItem fullScreenButton = new JMenuItem("Fullscreen");
		fullScreenButton.setActionCommand("Fullscreen");
		fullScreenButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				
				if(event.getActionCommand().equalsIgnoreCase("Fullscreen")){

					handler.getMangaViewerFrame().setFullscreen(true);

					//handler.getMangaViewerFrame().revalidate();
					fullScreenButton.setActionCommand("Exit Fullscreen");
					fullScreenButton.setName("Exit Fullscreen");
					fullScreenButton.setText("Exit Fullscreen");
				}else{
					//handler.getMangaViewerFrame().setExtendedState(JFrame.);
					
					handler.getMangaViewerFrame().setFullscreen(false);

					//handler.getMangaViewerFrame().revalidate();

					fullScreenButton.setActionCommand("Fullscreen");
					fullScreenButton.setName("Fullscreen");
					fullScreenButton.setText("Fullscreen");
				}
			}
			
		});
		
		menu.add(openFileButton);
		
		JMenuItem openURLButton = new JMenuItem("Load MangaHere Manga");
		openURLButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				handler.getMangaViewerFrame().remove(handler.getMangaViewerPanel());
				
				MangaMenuSelectionPanel leftPanel = new MangaMenuSelectionPanel(handler);
				
				//new ImageIcon(manga.getCover().getScaledInstance(topRightPanel.getWidth(), topRightPanel.getHeight(), Image.SCALE_SMOOTH))
				splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, handler.getMangaViewerPanel());
				splitPane.setResizeWeight(0.6);
				splitPane.setDividerLocation(handler.getMangaViewerFrame().getWidth()/2);
				splitPane.setDividerSize(0);
				splitPane.setEnabled(false);

				
				handler.getMangaViewerPanel().requestFocusInWindow();

				handler.getMangaViewerFrame().add(splitPane);
				handler.getMangaViewerFrame().revalidate();		
			}		
			
			
		});
		
		JMenuItem exitButton = new JMenuItem("Exit");
		exitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);				
			}
			
			
			
			
		});
		
		
		menu.add(openURLButton);
		
		
		menu.add(fullScreenButton);
		menu.add(previousChapterButton);
		menu.add(nextChapterButton);
		
		menu.add(exitButton);
		
		this.add(menu);
		
		titleMenu = new JMenu("MangaViewer");
		titleMenu.setFont(new Font(menu.getFont().getFontName(), 0, 16));

		this.add(titleMenu);
		
		
	}

	public JMenu getTitleMenu(){
		return titleMenu;
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}
	
	
}
