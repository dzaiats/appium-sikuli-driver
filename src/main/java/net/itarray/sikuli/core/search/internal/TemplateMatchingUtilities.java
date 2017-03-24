package net.itarray.sikuli.core.search.internal;

import net.itarray.sikuli.core.logging.ImageExplainer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class TemplateMatchingUtilities {
	
	public static class TemplateMatchResult extends Rectangle{
		public double score;
	}
	
	static ImageExplainer explainer = ImageExplainer.getExplainer(TemplateMatchingUtilities.class);
	
	public static IplImage computeTemplateMatchResultMatrix(IplImage input, IplImage target){

		int iwidth,iheight;
		if (input.roi() != null){
			iwidth = input.roi().width() - target.width() + 1;
			iheight = input.roi().height() - target.height() + 1;      
		}else{
			iwidth = input.width() - target.width() + 1;
			iheight = input.height() - target.height() + 1;
		}		
		IplImage map = IplImage.create(cvSize(iwidth,iheight), IPL_DEPTH_32F, target.nChannels());
		opencv_imgproc.cvMatchTemplate(input, target, map, CV_TM_CCOEFF_NORMED);
//		opencv_imgproc.cvMatchTemplate(input, target, map, CV_TM_SQDIFF_NORMED);
//		cvSubRS(map, cvScalarAll(1), map, null);
		
//		VisionUtils.negate(map, map);
//		cvAddS(map, cvScalarAll(255), map, null);
		return map;
	}
	
	static BufferedImage visualizeResultMatrix(IplImage result){
		IplImage resultToShow = IplImage.create(cvGetSize(result), 8, 1);		
		cvConvertScale(result, resultToShow, 255, 0);
		return new BufferedImage(resultToShow.width(), resultToShow.height(), 1);
	}
	
	public static IplImage computeTemplateMatchResultMatrixWithMultipleROIs(IplImage input, IplImage target, List<Rectangle> ROIs){

		int iwidth,iheight;
		cvResetImageROI(input);
		iwidth = input.width() - target.width() + 1;
		iheight = input.height() - target.height() + 1;

		IplImage result = IplImage.create(cvSize(iwidth,iheight), 32, 1);
		cvSet(result, cvScalarAll(0));
		
		for (Rectangle unboundedROI : ROIs){			
			if (unboundedROI.width < target.width()){
				// width too narrow to fit the target
				continue;
			}
			if (unboundedROI.height < target.height()){
				// height too short to fit the target
				continue;
			}
			
			// ROI should interesct with image bounds
			Rectangle roi = unboundedROI.intersection(new Rectangle(0,0,input.width(),input.height()));
						
			cvSetImageROI(input, cvRect(roi.x, roi.y, roi.width, roi.height));
			
			int w = roi.width - target.width() + 1;
			int h = roi.height - target.height() + 1;			
			cvSetImageROI(result, cvRect(roi.x, roi.y, w, h));

			cvMatchTemplate(input, target, result, CV_TM_CCOEFF_NORMED);
						
			cvResetImageROI(result);
			cvResetImageROI(input);
		}
		explainer.result(visualizeResultMatrix(result), "result with faster implementation");
		return result;		
	}
	
	public static IplImage computeTemplateMatchResultMatrixWithMultipleROIs_GrouthTruth(IplImage input, IplImage target, List<Rectangle> ROIs){

		int iwidth,iheight;
		
		cvResetImageROI(input);
		iwidth = input.width() - target.width() + 1;
		iheight = input.height() - target.height() + 1;
		
		explainer.step(input, "input");
		
		IplImage result = IplImage.create(cvSize(iwidth,iheight), 32, 1); 
		opencv_imgproc.cvMatchTemplate(input, target, result, CV_TM_CCOEFF_NORMED);
		
		explainer.step(visualizeResultMatrix(result), "raw result");

		IplImage keepMask = IplImage.create(cvSize(iwidth,iheight), 8, 1);
		cvSet(keepMask, cvScalarAll(0));
		
		for (Rectangle ROI : ROIs){			
			if (ROI.width < target.width()){
				// width too narrow to fit the target
				continue;
			}
			if (ROI.height < target.height()){
				// height too short to fit the target
				continue;
			}
			
			
			int w = Math.min(ROI.width, iwidth - ROI.x);
			int h = Math.min(ROI.height, iheight - ROI.y);
			w = w - target.width() + 1;
			h = h - target.height() + 1;
			CvRect cvROI = cvRect(ROI.x, ROI.y, w, h);
			cvSetImageROI(keepMask, cvROI);
			cvSet(keepMask, cvScalarAll(255));
			cvResetImageROI(keepMask);			
		}
		
		explainer.step(keepMask, "keep mask");
		
		IplImage maskedResult = IplImage.createCompatible(result);
		cvSet(maskedResult, cvScalarAll(0));
		cvCopy(result, maskedResult, keepMask);		
		explainer.result(visualizeResultMatrix(maskedResult), "match result with only ROIs");
		
		
		// pre-emptively release non-used IplImages
		maskedResult.release();
		keepMask.release();
		
		return result;
	}
	
	
	public static IplImage computeTemplateMatchResultMatrix1(IplImage input, IplImage target){

		int iwidth,iheight;
		if (input.roi() != null){
			iwidth = input.roi().width() - target.width() + 1;
			iheight = input.roi().height() - target.height() + 1;      
		}else{
			iwidth = input.width() - target.width() + 1;
			iheight = input.height() - target.height() + 1;
		}

		IplImage map = IplImage.create(cvSize(iwidth,iheight), 32, 1);      
		opencv_imgproc.cvMatchTemplate(input, target, map, CV_TM_SQDIFF_NORMED);
		return map;
	}
	
	public static TemplateMatchResult fetchNextBestMatch1(IplImage resultMatrix, IplImage target){
		DoublePointer min = new DoublePointer(1);
		DoublePointer max = new DoublePointer(1);
		CvPoint minPoint = new CvPoint(2);
		CvPoint maxPoint = new CvPoint(2);


		cvMinMaxLoc(resultMatrix, min, max, minPoint, maxPoint, null);

		double detectionScore = min.get(0);
		CvPoint detectionLoc = minPoint;

		TemplateMatchResult r = new TemplateMatchResult();
		r.x = detectionLoc.x();
		r.y = detectionLoc.y();
		r.width = target.width();
		r.height = target.height();
		r.score = detectionScore;
		//TemplateMatchResult r = new TemplateMatchResult();
		//TemplateMatchResult r = new Rectangle(detectionLoc.x(),detectionLoc.y(),target.width(),target.height());
		//r.score = (float) detectionScore;

		// Suppress returned match
		int xmargin = target.width()/3;
		int ymargin = target.height()/3;

		int x = detectionLoc.x();
		int y = detectionLoc.y();

		int x0 = Math.max(x-xmargin,0);
		int y0 = Math.max(y-ymargin,0);
		int x1 = Math.min(x+xmargin,resultMatrix.width());  // no need to blank right and bottom
		int y1 = Math.min(y+ymargin,resultMatrix.height());

		cvRectangle(resultMatrix, cvPoint(x0, y0), cvPoint(x1-1, y1-1), 
				cvRealScalar(1.0), CV_FILLED, 8,0);
		
		return r;
	}
	
	public static TemplateMatchResult fetchNextBestMatch(IplImage resultMatrix, IplImage target){
		DoublePointer min = new DoublePointer(1);
		DoublePointer max = new DoublePointer(1);
		CvPoint minPoint = new CvPoint(2);
		CvPoint maxPoint = new CvPoint(2);

		opencv_core.cvMinMaxLoc(resultMatrix, min, max, minPoint, maxPoint, null);

		double detectionScore = max.get(0);
		CvPoint detectionLoc = maxPoint;

		TemplateMatchResult r = new TemplateMatchResult();
		r.x = detectionLoc.x();
		r.y = detectionLoc.y();
		r.width = target.width();
		r.height = target.height();
		r.score = detectionScore;


		// Suppress returned match
		int xmargin = target.width()/3;
		int ymargin = target.height()/3;

		int x = detectionLoc.x();
		int y = detectionLoc.y();

		int x0 = Math.max(x-xmargin,0);
		int y0 = Math.max(y-ymargin,0);
		int x1 = Math.min(x+xmargin,resultMatrix.width());  // no need to blank right and bottom
		int y1 = Math.min(y+ymargin,resultMatrix.height());

		cvRectangle(resultMatrix, cvPoint(x0, y0), cvPoint(x1-1, y1-1),
				cvRealScalar(0.0), CV_FILLED, 8,0);
		
		return r;
	}
	
	
}