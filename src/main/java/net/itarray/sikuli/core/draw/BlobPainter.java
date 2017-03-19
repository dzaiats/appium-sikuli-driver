package net.itarray.sikuli.core.draw;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import org.bytedeco.javacpp.opencv_core.CvRect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class BlobPainter extends PiccoloImageRenderer {
	
	final List<CvRect> blobs;	
	public BlobPainter(BufferedImage input, List<CvRect> blobs){
		super(input);
		this.blobs = blobs;
	}
	
	@Override
	protected void addContent(PLayer layer) {
		for (CvRect blob : blobs){					
			PPath rect = PPath.createRectangle(blob.x(),blob.y(),blob.width(),blob.height());
			Random numGen = new Random();
			
			Color randomColor = new Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));					
			//rect.setStrokePaint(randomColor);
			
			rect.setStrokePaint(Color.cyan);
			
			
			rect.setStroke(new BasicStroke(5));				
			rect.setPaint(null);
			layer.addChild(rect);					
			
			PText sizeText = new PText("" + blob.width() + "x" + blob.height());
			sizeText.setOffset(blob.x(),blob.y());
			sizeText.setHorizontalAlignment(0f);
			sizeText.setPaint(Color.black);
			sizeText.setTextPaint(Color.white);
			layer.addChild(sizeText);					
		}
	}
}