/*******************************************************************************
 * Copyright 2011 sikuli.org
 * Released under the MIT license.
 * 
 * Contributors:
 *     Tom Yeh - initial API and implementation
 ******************************************************************************/
package net.itarray.sikuli.core.logging;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import net.itarray.sikuli.core.draw.ImageRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_core.IplImage;

class ImageExplanation {
	String imageLocalPath;
	Object message;
	ImageExplainer.Level level;
	long timestamp;
	ImageExplainer logger;
	BufferedImage image;
}

interface Appender {
	void doAppend(ImageExplanation event);
}


// This DefaulAppender writes explanation images to a local folder
class DefaultAppender implements Appender {

	private static final int MIN_CANVAS_HEIGHT = 30;
	private static final int MIN_CANVAS_WIDTH = 400;

	private int counter = 0;
	
	static final private String DefaultOutputDirectory = "log";
	
	static private File getOutputDirectory(){
		File outputDir = new File(DefaultOutputDirectory);
		if (!outputDir.exists())
			outputDir.mkdir();
		return outputDir;
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

	static BufferedImage paintMessageOnImage(BufferedImage input, Object message){
		final PCanvas canvas = new PCanvas();
		final PText text = new PText(message.toString());
		//        f.add(canvas);
		text.setOffset(0,0);
		text.setPaint(Color.white);
		text.setTextPaint(Color.black);
		text.setTransparency(0.9f);

		final PImage image = new PImage(input);
		image.setOffset(0,30);
		
		PPath imageBorder = PPath.createRectangle(0,30,(int)image.getWidth()-1,(int)image.getHeight()-1);
		imageBorder.setPaint(null);
		imageBorder.setStrokePaint(Color.black);
		imageBorder.setStroke(new BasicStroke(1f));
		
		canvas.getLayer().addChild(image);
		canvas.getLayer().addChild(text);
		canvas.getLayer().addChild(imageBorder);

		int w = Math.max(MIN_CANVAS_WIDTH, input.getWidth());
		int h = Math.max(MIN_CANVAS_HEIGHT, input.getHeight()+30);
		canvas.setBounds(0,0,w,h);

		return createComponentImage(canvas);
	}

	public void doAppend(ImageExplanation event){
		// immediately write to the disk
		String filename = "explain." + event.logger.getName() + "." + counter + ".png";
		try {

			BufferedImage imageWithMessage = paintMessageOnImage(event.image, event.message);

			File imageFile = new File(getOutputDirectory(), filename);
			
			ImageIO.write(imageWithMessage, "png", imageFile);			
			event.imageLocalPath = filename;
			// release the reference to the bufferedimage so it can be garbage collected
			event.image = null;

		} catch (IOException e) {
		} 		
		counter++;
	}

}

public class ImageExplainer {
	
	static public class Level {
		static final int ALL_INT = 100;
		static final int STEP_INT = 2;
		static final int RESULT_INT = 1;
		static final int OFF_INT = 0;	

		final private int levelInt;
		final private String levelString;
		Level(int level, String levelString){
			this.levelInt = level;
			this.levelString = levelString;
		}

		public boolean isGreaterOrEqual(Level anotherLevel){
			return this.levelInt >= anotherLevel.levelInt;
		}

		static public Level ALL = new Level(ALL_INT,"ALL");
		static public Level STEP = new Level(STEP_INT,"STEP");
		static public Level RESULT = new Level(RESULT_INT,"RESULT");
		static public Level OFF = new Level(OFF_INT,"OFF");

	}

	final private Appender appender = new DefaultAppender();

	final private String name;
	private Level level = Level.OFF;

	class ImageLogRecord{
		String title = "";
		String image_filename = "";
		String description = "";
	}

	public ImageExplainer(String name){
		this.name = name;
	}

	public void setLevel(Level level){
		this.level = level;
	}

	public void result(IplImage image, Object message){
		result(image,message);
	}
	
	public void step(ImageRenderer producer, Object message){
		if (level.isGreaterOrEqual(Level.STEP)){
			step(producer.render(), message);
		}
	}
	
	public void result(ImageRenderer producer, Object message){
		if (level.isGreaterOrEqual(Level.RESULT)){
			result(producer.render(), message);
		}
	}

	public void step(IplImage image, Object message){
		step(image,message);
	}

	public void step(BufferedImage image, Object message){	
		if (level.isGreaterOrEqual(Level.STEP)){
			ImageExplanation event = new ImageExplanation();
			event.level = Level.STEP;
			event.logger = this;
			event.message = message;
			event.timestamp = new Date().getTime();
			event.image = image;
			appender.doAppend(event);
		}
	}

	public void result(BufferedImage image, Object message){
		if (level.isGreaterOrEqual(Level.RESULT)){
			ImageExplanation event = new ImageExplanation();
			event.level = Level.RESULT;
			event.logger = this;
			event.message = message;
			event.timestamp = new Date().getTime();
			event.image = image;
			appender.doAppend(event);
		}
	}

	public String getName() {
		return name;
	}

	final static Map<String,ImageExplainer> explainers = new HashMap<String,ImageExplainer>();
	
	static public ImageExplainer getExplainer(Class clazz){
		return getExplainer(clazz.getCanonicalName());		
	}
	
	static public ImageExplainer getExplainer(String name){
		ImageExplainer explainer;
		if (explainers.containsKey(name)){
			explainer = explainers.get(name);
		}else{
			explainer = new ImageExplainer(name);
			explainers.put(name, explainer);
		}
		return explainer;
	}
	
}
