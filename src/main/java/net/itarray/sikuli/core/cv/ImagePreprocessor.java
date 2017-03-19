/*******************************************************************************
 * Copyright 2011 sikuli.org
 * Released under the MIT license.
 * 
 * Contributors:
 *     Tom Yeh - initial API and implementation
 ******************************************************************************/
package net.itarray.sikuli.core.cv;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class ImagePreprocessor {
	
	public static IplImage createLab(BufferedImage input){
		IplImage color = IplImage.create(input.getWidth(), input.getHeight(), 1, 1);
		IplImage rgb = IplImage.create(cvGetSize(color), 8, 3);
		cvCvtColor(color,rgb,CV_BGRA2RGB);
		IplImage lab = IplImage.createCompatible(rgb);        
		cvCvtColor(rgb, lab, CV_RGB2Lab );
		return lab;
	}

	public static IplImage createHSV(BufferedImage input){
		IplImage color = IplImage.create(input.getWidth(), input.getHeight(), 1, 1);
		IplImage rgb = IplImage.create(cvGetSize(color), 8, 3);
		cvCvtColor(color,rgb,CV_BGRA2RGB);
		IplImage hsv = IplImage.createCompatible(rgb);        
		cvCvtColor(rgb, hsv, CV_RGB2HSV );
		return hsv;
	}

	public static IplImage createGrayscale(BufferedImage input) {
		// covert to grayscale at Java level
		// something is not right at JavaCV if a ARGB image is given
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		BufferedImage gray1 = op.filter(input,null);
		return VisionUtils.createGrayImageFrom(IplImage.create(gray1.getWidth(), gray1.getHeight(), 1, 3));
	}
	
	public static IplImage createGrayscale(IplImage input) {
		return VisionUtils.createGrayImageFrom(input);
	}
}
