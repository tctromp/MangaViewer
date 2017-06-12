package org.trompgames.mangaviewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MangaViewerPanel extends JPanel{
	
	private MangaViewerHandler handler;

	
	
	public MangaViewerPanel(MangaViewerHandler handler) {
		this.handler = handler;
		
	}
	

	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		
		
		Color flat = new Color(66, 73, 73);
		
		g2d.setColor(flat);		
		
		g2d.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		
		
		int scrollWidth = (int) Math.round(1.0 * this.getWidth()/3 * handler.getScrollWidthMultiplier());		

		
		if(handler.getCurrentManga() == null){
		
			Color darker = new Color(73, 80, 80);
			
			g2d.setColor(darker);	
			
			int border = 25;
			
			g2d.fillRect(this.getWidth()/2 - scrollWidth/2, border, scrollWidth - 1, this.getHeight() - 1 - 2*border);
			
			
			return;
		}
		
		BufferedImage image = handler.getCurrentImage();
		if(image == null){
			System.out.println("Error: Current image not found");
			return;
		}		
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		int buffer = 2;
		
		MangaFitType type = handler.getImageFitType(image);
		
		
		if(type.equals(MangaFitType.SCROLL)){
			fitScroll(g2d, image, imageWidth, imageHeight, scrollWidth);
		}else if(type.equals(MangaFitType.WIDTH)){
			fitWidth(g2d, image, imageWidth, imageHeight);			
		}else{			
			fitHeight(g2d, image, imageWidth, imageHeight, buffer);		
		}
		
		

	}
	

	
	public void fitWidth(Graphics2D g2d, BufferedImage image, int imageWidth, int imageHeight){
		int newHeight = (int) Math.round(1.0 * this.getWidth() * (1.0 * imageHeight/imageWidth));

		g2d.drawImage(image, 0, -handler.getYOffset(), this.getWidth(), newHeight, null);
		
		
	}
	
	public void fitScroll(Graphics2D g2d, BufferedImage image, int imageWidth, int imageHeight, int scrollWidth){
		int newHeight = (int) Math.round(1.0 * scrollWidth * (1.0 * imageHeight/imageWidth));

		int xOffset = (int) Math.round(1.0 * this.getWidth()/2 - 1.0 * scrollWidth/2);	
		g2d.drawImage(image, xOffset, -handler.getYOffset(), scrollWidth, newHeight, null);
		
		
	}
	
	public void fitHeight(Graphics2D g2d, BufferedImage image, int imageWidth, int imageHeight, int buffer){
		
		int height = this.getHeight() - 2*buffer;
		
		//int newWidth = (int) Math.round(1.0 * this.getHeight() * (1.0 * imageWidth/imageHeight));
		int newWidth = (int) Math.round(1.0 * height * (1.0 * imageWidth/imageHeight));

		
		if(newWidth > this.getWidth()){
			fitWidth(g2d, image, imageWidth, imageHeight);
			return;
		}
		
		int xOffset = (int) Math.round(1.0 * this.getWidth()/2 - 1.0 * newWidth/2);
		
		g2d.drawImage(image, xOffset, buffer - handler.getYOffset(), newWidth, height - 1, null);
	}
	
	
	
	
}
