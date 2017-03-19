package net.itarray.sikuli.core.draw;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolox.nodes.PShadow;
import net.itarray.sikuli.core.cv.VisionUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class PiccoloImageRenderer implements ImageRenderer {
	
	final private BufferedImage input;
	public PiccoloImageRenderer(BufferedImage input){
		this.input = input;
	}
	
	public PiccoloImageRenderer(int width, int height){
		this.input = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	}
	
	static public void addNodeWithShadow(PNode parent, PNode node){
		
		int blurRadius = 4;
		PShadow shadowNode = new PShadow(node.toImage(), Color.gray, blurRadius);

		double tx = node.getX() + node.getXOffset();
		double ty = node.getY() + node.getYOffset();
		
		shadowNode.setOffset(tx - (2 * blurRadius) + 1.0d, ty - (2 * blurRadius) + 1.0d);
		parent.addChild(shadowNode);
		parent.addChild(node);
	}
	
	public BufferedImage render(){
		final PCanvas canvas = new PCanvas();
		final PImage image = new PImage(input);
		canvas.getLayer().addChild(image);
		addContent(canvas.getLayer());        
		canvas.setBounds(0,0,input.getWidth(),input.getHeight());        
		return VisionUtils.createComponentImage(canvas);
	}
	
	abstract protected void addContent(PLayer layer);
};