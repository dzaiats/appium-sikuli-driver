package net.itarray.sikuli.api.event;

import net.itarray.sikuli.api.DesktopScreenRegion;
import net.itarray.sikuli.api.Target;

import java.util.EventListener;

/**
 * Interface for a screen region to register to receive notifications
 *  when the state of this screen region has changed. Each state is 
 *  defined by the presence of a particular target. 
 * 
 * @author Tom Yeh (tom.yeh@colorado.edu)
 * @see DesktopScreenRegion#addTargetEventListener(Target, TargetEventListener)
 */
public interface StateChangeListener extends EventListener {	
	/**
	 * Invoked when the state of a ScreenRegion has changed.
	 * 
	 * @param event a StateChangeEvent object
	 */
	public void stateChanged(StateChangeEvent event);
}