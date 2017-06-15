package org.trompgames.mangaviewer;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import org.trompgames.onlinemanga.MangaHereManga;
import org.trompgames.onlinemanga.OnlineManga;
import org.trompgames.onlinemanga.OnlineMangaChapter;

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
		fc.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {				
				handler.loadManga(fc.getSelectedFile());
				handler.reload();
			}			
		});
		
		//fc.showOpenDialog(this);
				
		
		
		//Add panel
		panel = new MangaViewerPanel(handler);
		this.add(panel);
		
		
		
		//Add Listeners
		addMouseWheelListener();
		addMouseListener();
		addKeyListener();
		
		
		
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Manga");
		
		JMenuItem openFileButton = new JMenuItem("Open");
		openFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				fc.showOpenDialog(handler.getMangaViewerFrame());				
			}
			
		});
		menu.add(openFileButton);
		
		JMenuItem openURLButton = new JMenuItem("Load MangaHere Manga");
		openURLButton.addActionListener(new ActionListener(){

			private JSplitPane splitPane;
			private JList<String> chapterList = new JList<String>();
			private JList<String> mangaList;
			private DefaultListModel<String> mangaListModel;
			
			private JTextArea detailField;
			
			private ArrayList<MangaHereManga> mangas;
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				//String url = JOptionPane.showInputDialog(handler.getMangaViewerFrame(), "MangaHere URL: ", null);
				//if(url == null) return;
				
				//String url = "http://www.mangahere.co/manga/uq_holder/";
				
				//MangaHereManga manga = new MangaHereManga(url);				
				
				//if(!manga.hasValidURL()){
				//	JOptionPane.showMessageDialog(handler.getMangaViewerFrame(), "Invalid URL", "Error: ", JOptionPane.ERROR_MESSAGE);
				//	return;
				//}
				
					
				
				handler.getMangaViewerFrame().remove(handler.getMangaViewerPanel());
				
				JPanel leftPanel = new JPanel();
				leftPanel.setLayout(new GridLayout(3, 0));
				
						

				
				
				mangas = MangaProperties.getOnlineMangas();

				
				
				loadTopPanel(leftPanel);
				
				MangaHereManga selectedManga = mangas.get(mangaList.getSelectedIndex());
				
				loadMidPanel(leftPanel, selectedManga);
				loadBotPanel(leftPanel, selectedManga);

				
				
				


				
				
				//new ImageIcon(manga.getCover().getScaledInstance(topRightPanel.getWidth(), topRightPanel.getHeight(), Image.SCALE_SMOOTH))
				splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, handler.getMangaViewerPanel());
				splitPane.setResizeWeight(0.6);
				splitPane.setDividerLocation(handler.getMangaViewerFrame().getWidth()/2);
				splitPane.setDividerSize(0);
				splitPane.setEnabled(false);

				
				handler.getMangaViewerPanel().requestFocusInWindow();

				handler.getMangaViewerFrame().add(splitPane);
				handler.getMangaViewerFrame().revalidate();
				
				
				//handler.getMangaViewerFrame().repaint();
				//handler.getMangaViewerFrame().repaint();
				//splitPane.repaint();
				
				/*
				String url = JOptionPane.showInputDialog(handler.getMangaViewerFrame(), "MangaHere URL: ", null);
				if(url == null) return;
				
				handler.setOnlineManga(url);
				*/
			}		
			
			private void loadTopPanel(JPanel leftPanel){
				JPanel topPanel = new JPanel();
				

				mangaListModel = new DefaultListModel<String>();
				
				for(MangaHereManga m : mangas){
					mangaListModel.addElement(m.getTitle());					
				}
				
				mangaList = new JList<String>(mangaListModel);
				
				mangaList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				
				mangaList.setFont(new Font(mangaList.getFont().getName(), 0, 16));
				mangaList.setBackground(null);

				JScrollPane listScroller = new JScrollPane(mangaList);
				
				listScroller.getViewport().getView().addMouseListener(new MouseAdapter(){

					@Override
					public void mouseClicked(MouseEvent event) {
						//System.out.println("Clicky: " + mangaList.getSelectedIndex());
						loadMidPanelData(mangas.get(mangaList.getSelectedIndex()));
						loadBotPanelData(mangas.get(mangaList.getSelectedIndex()));
					}			
					
				});
				
				
				mangaList.setSelectedIndex(0);
				
				topPanel.add(listScroller);
				
				JButton button = new JButton("Read");
				button.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent event) {
						
						handler.getMangaViewerFrame().remove(splitPane);
						handler.getMangaViewerFrame().add(handler.getMangaViewerPanel());
						handler.getMangaViewerFrame().revalidate();
						
						handler.setCurrentChapter(chapterList.getSelectedIndex());
						
						
						
						handler.setOnlineManga(mangas.get(mangaList.getSelectedIndex()), chapterList.getSelectedIndex());
						
						
						handler.getMangaViewerPanel().requestFocusInWindow();
						
						//handler.getProperties().loadJSON(manga);
						
						
						
					}				
					
				});
				
				JButton loadButton = new JButton("Load");
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
				
				
				topPanel.add(button);
				topPanel.add(loadButton);
				leftPanel.add(topPanel);
			}

			private void loadBotPanelData(MangaHereManga manga){
				MangaProperties properties = new MangaProperties(handler, manga.getManga());
				
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
				
				chapterList.setModel(model);
				chapterList.setSelectedIndex(currentChapter);

			}
			
			private void loadBotPanel(JPanel leftPanel, MangaHereManga manga){
				JPanel botPanel = new JPanel();
				botPanel.setBorder(BorderFactory.createTitledBorder("Chapters: "));
				botPanel.setLayout(new GridLayout(0, 1));
				
				
				loadBotPanelData(manga);				
				
				chapterList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				//list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				//list.setVisibleRowCount(-1);
				chapterList.setFont(new Font(chapterList.getFont().getName(), 0, 20));
				chapterList.setBackground(null);

				JScrollPane listScroller = new JScrollPane(chapterList);		
				
				//list.setval
				
				listScroller.getViewport().getView().addMouseListener(new MouseAdapter(){

					@Override
					public void mouseClicked(MouseEvent event) {
						//System.out.println("Clicky");
					}			
					
				});
				
				botPanel.add(listScroller);
				leftPanel.add(botPanel);			
			}	
			
			
			private void loadMidPanelData(MangaHereManga manga){
				
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
				detailField.setText(details);

			}
			
			private void loadMidPanel(JPanel leftPanel, MangaHereManga manga){
				JPanel midPanel = new JPanel();
				midPanel.setLayout(new GridLayout(0, 2));
				//midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.LINE_AXIS));

				JPanel topLeftPanel = new JPanel();
				JPanel topRightPanel = new JPanel();
				
				if(manga.getCover() != null)
					topRightPanel.add(new JLabel(new ImageIcon(manga.getCover())));		
				
				topLeftPanel.setBorder(BorderFactory.createTitledBorder("Info: "));
				topRightPanel.setBorder(BorderFactory.createTitledBorder("Cover: "));
				
				
				topLeftPanel.setLayout(new GridLayout(0, 1));
				
				
				
				detailField = new JTextArea();
				
				loadMidPanelData(manga);

				
				detailField.setHighlighter(null);
				detailField.setEditable(false);
				
				detailField.setLineWrap(true);
				detailField.setWrapStyleWord(true);
				
				detailField.setFont(new Font(detailField.getFont().getFontName(), 0, 18));
				detailField.setBackground(null);
				
				topLeftPanel.add(detailField);
				
				topRightPanel.setLayout(new GridLayout(0, 1));
				
				midPanel.add(topLeftPanel);
				midPanel.add(topRightPanel);
				leftPanel.add(midPanel);

			}
			
		});
		
		menu.add(openURLButton);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);

		
		
		

		
		//Set close operation and show frame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		
	}
	
	
	
	public void updateTitle(){
		Manga m = handler.getCurrentManga();
		
		if(m == null){
			this.setTitle("MangaViewer");
			return;
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
			
			this.setTitle("MangaViewer - " + chapter.getName() + " - Page: (" + (handler.getCurrentPage() + 1) + "/" + chapter.getPages() + ") - " + cumPages + "/" + manga.getTotalPages() + " - " + s);
		}else{
			
			OnlineManga manga = (OnlineManga) m;
			
			this.setTitle("MangaViewer - " +  " - Page: (" + (handler.getCurrentPage() + 1) + "/" + manga.getPages(handler.getCurrentChapter()) + ")");
		}

		
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
	
}
