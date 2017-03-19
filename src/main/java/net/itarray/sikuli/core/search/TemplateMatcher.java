package net.itarray.sikuli.core.search;

import com.google.common.collect.Lists;
import net.itarray.sikuli.core.cv.ImagePreprocessor;
import net.itarray.sikuli.core.search.internal.TemplateMatchingUtilities;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import net.itarray.sikuli.core.search.internal.TemplateMatchingUtilities.TemplateMatchResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.IplImage;


public class TemplateMatcher {

	public static class Result {

		public int x;
		public int y;
		public int width;
		public int height;	
		private double score;	

		public Result(Rectangle r) {
			super();
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
		}

		public Rectangle getBounds() {
			return new Rectangle(x,y,width,height);
		}

		public int getX(){
			return x;
		}

		public int getY(){
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public Point getLocation() {
			return getBounds().getLocation();
		}
		
		public double getScore(){
			return score;
		}
		public void setScore(double score) {
			this.score = score;
		}
		
	}	

	public static List<Result> findMatchesByGrayscaleAtOriginalResolution(BufferedImage input, BufferedImage target, int limit, double minScore){
		OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
		Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
		IplImage iplImageInput = iplConverter.convert(java2dConverter.convert(input));
		IplImage iplImageTarget = iplConverter.convert(java2dConverter.convert(target));
		IplImage input1 = ImagePreprocessor.createGrayscale(iplImageInput);
		IplImage target1 = ImagePreprocessor.createGrayscale(iplImageTarget);
		IplImage resultMatrix = TemplateMatchingUtilities.computeTemplateMatchResultMatrix(input1, target1);
		List<Result> result = fetchMatches(resultMatrix, target1, limit, minScore);
		input1.release();
		target1.release();
		resultMatrix.release();
		return result;
	}

	// Experimental
	public static List<Result> findMatchesByGrayscaleAtOriginalResolutionWithROIs(
			BufferedImage input, BufferedImage target, int limit, double minScore, List<Rectangle> rois){
		IplImage input1 = ImagePreprocessor.createGrayscale(input);
		IplImage target1 = ImagePreprocessor.createGrayscale(target);
		IplImage resultMatrix = TemplateMatchingUtilities.computeTemplateMatchResultMatrixWithMultipleROIs(input1, target1, rois);
		return fetchMatches(resultMatrix, target1, limit, minScore);
	}


	static private List<Result> fetchMatches(IplImage resultMatrix, IplImage target, int limit, double minScore){
		List<Result> matches = Lists.newArrayList();		
		while(matches.size() < limit){
			TemplateMatchResult result = TemplateMatchingUtilities.fetchNextBestMatch(resultMatrix, target);
			Result m = new Result(result);
			m.setScore(result.score);
			if (m.getScore() >= minScore){
				matches.add(m);
			}else{
				break;
			}
		}
		return matches;
	}

}
