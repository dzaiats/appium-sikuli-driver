package net.itarray.sikuli.api.visual;

import com.google.common.collect.Lists;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import net.itarray.sikuli.api.SikuliRuntimeException;
import net.itarray.sikuli.api.visual.element.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ImageCanvas extends Canvas {

	private BufferedImage backgroundImage;
	
	public ImageCanvas(URL imageUrl){
		try {
			backgroundImage = ImageIO.read(imageUrl);
		} catch (IOException e) {
			throw new SikuliRuntimeException(e);
		}
	}
	
	public ImageCanvas(BufferedImage image){
		backgroundImage = image;
	}

	public void display(int seconds){
	}

	public void display(double seconds){
	}
	
	public void displayWhile(Runnable runnable){
	}
	
	List<ScreenDisplayable> displayableList = Lists.newArrayList();
	public void show(){
	}
	
	public void hide(){
	}	
	
	public BufferedImage createImage(){
		final PCanvas canvas = new PCanvas();
		final PLayer layer = canvas.getLayer();

		final PImage background = new PImage(backgroundImage);
		layer.addChild(background);
		canvas.setBounds(0,0,backgroundImage.getWidth(),backgroundImage.getHeight());

		PLayer foregroundLayer = new PLayer();		
		layer.addChild(foregroundLayer);		
		layer.addChild(foregroundLayer);

		for (Element element : getElements()){
			PNode node = PNodeFactory.createFrom(element);
			foregroundLayer.addChild(node);
		}
				
		return createComponentImage(canvas);
		
		// add memory release stuff
	}

	static public BufferedImage createComponentImage(Component component) {
		Dimension size = component.getSize();
		BufferedImage image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		component.paint(g2);
		g2.dispose();
		return image;
	}

}

