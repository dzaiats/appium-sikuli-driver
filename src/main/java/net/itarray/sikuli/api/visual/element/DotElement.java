package net.itarray.sikuli.api.visual.element;

import java.awt.*;


public class DotElement extends Element {
	
	public DotElement(){
		super();
		setColor(Color.red);
	}
	
	@Override
	public void setLineColor(Color color){
		setColor(color);
	}

}